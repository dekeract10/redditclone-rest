package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityDto;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CommunityService {
    @Transactional
    Community save(Community community);
    @Transactional
    Optional<Community> findById(UUID id);
    @Transactional
    Optional<Community> findByName(String communityName);
    @Transactional
    Community createCommunity(CommunityDto communityDto, String username);
    @Transactional
    Iterable<Community> findAll();
    @Transactional
    Optional<Community> findCommunityByName(String communityName);

    Community update(Community community);
}
