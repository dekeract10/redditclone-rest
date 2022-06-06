package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Comment;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommentRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public int getCountByPostId(UUID postId) {
        return commentRepository.getCommentCountByPostId(postId);
    }

    @Override
    public Optional<Comment> findCommentById(UUID commentId) {
        return commentRepository.findById(commentId);
    }
}
