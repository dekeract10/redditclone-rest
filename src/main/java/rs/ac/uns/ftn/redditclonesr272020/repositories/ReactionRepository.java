package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;

import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    public Iterable<Reaction> findAllByPost(UUID postId);

    @Query(value = "SELECT COALESCE(SUM(IF(STRCMP('UPVOTE', r.reaction_type) = 0, 1, -1)), 0) FROM reaction r WHERE post = ?1", nativeQuery = true)
    public int getKarmaForPost(UUID postId);

    @Query(value = "SELECT COALESCE(SUM(IF(STRCMP('UPVOTE', r.reaction_type) = 0, 1, -1)), 0) FROM reaction r WHERE comment = ?1", nativeQuery = true)
    public int getKarmaForComment(UUID postId);
}