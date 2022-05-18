package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;

import java.util.UUID;

@Service
public interface ReactionService {
    public Iterable<Reaction> findAllByPost(UUID postId);
    public int getPostKarma(UUID postId);
    public int getCommentKarma(UUID commentId);
}
