package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;

import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, UUID> {
    Optional<Community> findByName(String communityName);
}
