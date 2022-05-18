package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.redditclonesr272020.converters.PostListConverter;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.CommunityNonExistentException;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UserBannedException;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostListDto;
import rs.ac.uns.ftn.redditclonesr272020.services.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private FlairService flairService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @Transactional
    public ResponseEntity<String> createPost(@Valid @RequestBody PostDto postDto, BindingResult result, Authentication authentication) {
        // Initial checks to validate entity
        if (result.hasErrors())
            return ResponseEntity.badRequest().body("Post is not valid");
        if (postDto.getImagePath() == null && postDto.getText() == null)
            return ResponseEntity.badRequest().body("Post must have either text or image");

        var author = userService.findUserByUsername(authentication.getName());
        if (author.isEmpty())
            return ResponseEntity.badRequest().body("User is not valid");

        var community = communityService.findCommunityByName(postDto.getCommunityName());
        if (community.isEmpty())
            return ResponseEntity.badRequest().body("Community is not valid");

        Iterable<Flair> flairs = flairService.findAllById(postDto.getFlairs());
        Set<Flair> postFlairs = new HashSet<>();
        for (var flair : flairs){
            postFlairs.add(flair);
        }

        // Create the entity
        Post post = new Post();
        post.setFlairs(postFlairs);
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText() == null ? "" : postDto.getText());
        post.setImagePath(postDto.getImagePath());
        post.setUser(author.get());
        post.setCreationDate(LocalDate.now());

        try {
            community.get().getPosts().add(post);
            communityService.update(community.get());
            return ResponseEntity.ok().body(post.getId().toString());
        } catch (CommunityNonExistentException e) {
            return ResponseEntity.badRequest().body("Community not valid");
        } catch (UserBannedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is banned in this community");
        }
    }

    @GetMapping
    @Transactional
    public ResponseEntity<Iterable<PostListDto>> getAllPosts(){
        var postListConverter = new PostListConverter();
        var posts = postService.getAllPosts();
        var postDtos = new HashSet<PostListDto>();
        for (var post : posts) {
            var postDto = postListConverter.toDto(post);
            postDto.setKarma(reactionService.getPostKarma(post.getId()));
            postDto.setCommentCount(commentService.getCountByPostId(post.getId()));
            postDtos.add(postDto);
        }
        return ResponseEntity.ok(postDtos);
    }
}
