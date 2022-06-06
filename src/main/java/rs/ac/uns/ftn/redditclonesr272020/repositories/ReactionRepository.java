package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.redditclonesr272020.model.Comment;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;
import rs.ac.uns.ftn.redditclonesr272020.model.User;

import java.util.Optional;
import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    public Iterable<Reaction> findAllByPost(UUID postId);

    public Optional<Reaction> findByUserAndPost(User user, Post post);
    public Optional<Reaction> findByUserAndComment(User user, Comment post);

    @Query(value = "SELECT COALESCE(SUM(IF(STRCMP('UPVOTE', r.reaction_type) = 0, 1, -1)), 0) FROM reaction r WHERE post = ?1", nativeQuery = true)
    public int getKarmaForPost(UUID postId);

    @Query(value = "SELECT COALESCE(SUM(IF(STRCMP('UPVOTE', r.reaction_type) = 0, 1, -1)), 0) FROM reaction r WHERE comment = ?1", nativeQuery = true)
    public int getKarmaForComment(UUID postId);

}