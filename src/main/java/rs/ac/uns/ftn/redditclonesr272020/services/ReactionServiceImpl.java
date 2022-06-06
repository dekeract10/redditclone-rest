package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Comment;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;
import rs.ac.uns.ftn.redditclonesr272020.repositories.ReactionRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReactionServiceImpl implements ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Override
    public Iterable<Reaction> findAllByPost(UUID postId) {
        return reactionRepository.findAllByPost(postId);
    }

    @Override
    public int getPostKarma(UUID postId) {
        return reactionRepository.getKarmaForPost(postId);
    }

    @Override
    public int getCommentKarma(UUID commentId) {
        return reactionRepository.getKarmaForComment(commentId);
    }

    @Override
    public Optional<Reaction> findByUserAndPost(String username, UUID postId) {
        var user = userService.findUserByUsername(username);
        if (user.isEmpty())
            return Optional.empty();

        var post = postService.findById(postId);
        if (post.isEmpty())
            return Optional.empty();

        return reactionRepository.findByUserAndPost(user.get(), post.get());
    }

    @Override
    @Transactional
    public Reaction save(Reaction reaction) {
        if (reaction.getPost() != null){
            var found = reactionRepository.findByUserAndPost(reaction.getUser(), reaction.getPost());
            found.ifPresent(r -> reaction.setId(r.getId()));
        } else {
            var found = reactionRepository.findByUserAndComment(reaction.getUser(), reaction.getComment());
            found.ifPresent(r -> reaction.setId(r.getId()));
        }
        return reactionRepository.save(reaction);
    }

    @Override
    @Transactional
    public void delete(Reaction reaction) {
        reactionRepository.delete(reaction);
    }
}
