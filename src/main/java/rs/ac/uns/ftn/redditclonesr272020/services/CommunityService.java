package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityDto;

import java.util.Optional;
import java.util.UUID;

@Service
public interface CommunityService {
    Community save(Community community);
    Optional<Community> findById(UUID id);
    Optional<Community> findByName(String communityName);

    Community createCommunity(CommunityDto communityDto, String username);

    Iterable<Community> findAll();
}
