package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.redditclonesr272020.model.Reaction;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CreateReactionDto;
import rs.ac.uns.ftn.redditclonesr272020.services.CommentService;
import rs.ac.uns.ftn.redditclonesr272020.services.PostService;
import rs.ac.uns.ftn.redditclonesr272020.services.ReactionService;
import rs.ac.uns.ftn.redditclonesr272020.services.UserService;

@RestController
@RequestMapping("api/reactions")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_MOD", "ROLE_ADMIN"})
    public ResponseEntity<String> createReaction(@RequestBody CreateReactionDto reactionDto, BindingResult result, Authentication auth) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce("", (s, s2) -> s + s2));

        var user = userService.findUserByUsername(auth.getName());
        if (user.isEmpty())
            return ResponseEntity.badRequest().body("User not found");

        if (reactionDto.getPostId() == null && reactionDto.getCommentId() == null)
            return ResponseEntity.badRequest().body("Must specify post or comment to react to");

        if (reactionDto.getPostId() != null) {
            return savePostReaction(reactionDto, user.get());
        } else {
            return saveCommentReaction(reactionDto, user.get());
        }

    }

    private ResponseEntity<String> savePostReaction(CreateReactionDto reactionDto, User user) {
        var post = postService.findById(reactionDto.getPostId());
        if (post.isEmpty())
            return ResponseEntity.badRequest().body("Post not found");


        var reaction = new Reaction();
        reaction.setPost(post.get());
        reaction.setUser(user);
        reaction.setReactionType(reactionDto.getReactionType());
        var newReaction = reactionService.save(reaction);

        return ResponseEntity.ok(newReaction.getId().toString());
    }
    private ResponseEntity<String> saveCommentReaction(CreateReactionDto reactionDto, User user) {
        var comment = commentService.findCommentById(reactionDto.getCommentId());
        if (comment.isEmpty())
            return ResponseEntity.badRequest().body("Comment not found");

        var reaction = new Reaction();
        reaction.setComment(comment.get());
        reaction.setUser(user);
        reaction.setReactionType(reactionDto.getReactionType());

        var newReaction = reactionService.save(reaction);

        return ResponseEntity.ok(newReaction.getId().toString());
    }
}
