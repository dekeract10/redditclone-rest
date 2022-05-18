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
    @Transactional
    public Community save(Community community) {
        return communityRepository.save(community);
    }

    @Override
    @Transactional
    public Optional<Community> findById(UUID id) {
        return communityRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Community> findByName(String communityName) {
        return communityRepository.findByName(communityName);
    }

    @Transactional
    private void saveNewCommunity(Community community, Moderator moderator) {
        communityRepository.save(community);
        userService.update(moderator);
    }

    @Override
    @Transactional
    public Community createCommunity(CommunityDto communityDto, String username) {
        var rules = new ArrayList<Rule>();
        for (var rule : communityDto.getRules()) {
            rules.add(new Rule(rule));
        }

        Set<Moderator> moderators = new HashSet<>();
        var newModerator = userService.makeModerator(username);
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
    @Transactional
    public Iterable<Community> findAll() {
        return communityRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Community> findCommunityByName(String communityName) {
        return communityRepository.findByName(communityName);
    }

    @Override
    @Transactional
    public Community update(Community community) {
        return communityRepository.save(community);
    }
}
