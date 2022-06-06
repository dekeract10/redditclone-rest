package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ReactionService {
    public Iterable<Reaction> findAllByPost(UUID postId);

    public int getPostKarma(UUID postId);

    public int getCommentKarma(UUID commentId);

    public Optional<Reaction> findByUserAndPost(String username, UUID postId);

    @Transactional
    public Reaction save(Reaction reaction);

    @Transactional
    public void delete(Reaction reaction);
}
