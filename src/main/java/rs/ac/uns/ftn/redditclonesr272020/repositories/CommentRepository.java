package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.redditclonesr272020.model.Comment;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query(value = "SELECT COUNT(c.comment_id) FROM comment c WHERE post = ?1", nativeQuery = true)
    int getCommentCountByPostId(UUID postId);
}