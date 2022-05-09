package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Moderator;

import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository extends CrudRepository<Community, UUID> {
    Optional<Community> findByName(String communityName);
}
