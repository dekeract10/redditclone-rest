package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Comment;

import java.util.Optional;
import java.util.UUID;

@Service
public interface CommentService {
    int getCountByPostId(UUID postId);

    Optional<Comment> findCommentById(UUID commentId);
}
