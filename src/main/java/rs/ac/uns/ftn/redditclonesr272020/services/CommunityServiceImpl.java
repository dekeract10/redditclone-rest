package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Moderator;
import rs.ac.uns.ftn.redditclonesr272020.model.Rule;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityDto;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommunityRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserService userService;

    @Override
    public Community save(Community community) {
        return communityRepository.save(community);
    }

    @Override
    public Optional<Community> findById(UUID id) {
        return communityRepository.findById(id);
    }

    @Override
    public Optional<Community> findByName(String communityName) {
        return communityRepository.findByName(communityName);
    }

    @Transactional
    private void saveNewCommunity(Community community, Moderator moderator) {
        communityRepository.save(community);
        userService.update(moderator);
    }

    @Override
    public Community createCommunity(CommunityDto communityDto, String username) {
        Set<Moderator> moderators = new HashSet<>();
        var creatorOpt = userService.findUserByUsername(username);
        if (creatorOpt.isEmpty())
            throw new UsernameNotFoundException("User with username not found");

        var creator = creatorOpt.get();
//        Moderator moderator = null;
//        if (creator instanceof Moderator) {
//            moderators.add((Moderator) creator);
//        } else {
//            moderator = new Moderator();
//
//            moderator.setUsername(creator.getUsername());
//            moderator.setId(creator.getId());
//            moderator.setEmail(creator.getEmail());
//            moderator.setPassword(creator.getPassword());
//            moderator.setDescription(creator.getDescription());
//            moderator.setAvatar(creator.getAvatar());
//            moderator.setRegistrationDate(creator.getRegistrationDate());
//            moderator.setDisplayName(creator.getDisplayName());
//            moderator.setId(creator.getId());
//            moderator.setBans(creator.getBans());
//
//            moderators.add(moderator);
//        }


        var rules = new ArrayList<Rule>();
        for (var rule : communityDto.getRules()) {
            rules.add(new Rule(rule));
        }

        userService.makeModerator(creator);
        var newModerator = (Moderator)userService.findUserById(creator.getId());
        moderators.add(newModerator);

        var community = new Community();
        community.setCreationDate(LocalDate.now());
        community.setName(communityDto.getName());
        community.setDescription(communityDto.getDescription());
        community.setModerators(moderators);
        community.setRules(rules);

        communityRepository.save(community);
        return community;
    }

    @Override
    public Iterable<Community> findAll() {
        return communityRepository.findAll();
    }
}
