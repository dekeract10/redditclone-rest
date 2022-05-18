package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;
import rs.ac.uns.ftn.redditclonesr272020.repositories.ReactionRepository;

import java.util.UUID;

@Service
public class ReactionServiceImpl implements ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;
    @Override
    public Iterable<Reaction> findAllByPost(UUID postId) {
        return findAllByPost(postId);
    }

    @Override
    public int getPostKarma(UUID postId) {
        return reactionRepository.getKarmaForPost(postId);
    }

    @Override
    public int getCommentKarma(UUID commentId) {
        return reactionRepository.getKarmaForComment(commentId);
    }
}
