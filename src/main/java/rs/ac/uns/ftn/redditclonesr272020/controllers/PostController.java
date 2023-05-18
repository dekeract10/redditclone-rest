package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.redditclonesr272020.converters.FullPostConverter;
import rs.ac.uns.ftn.redditclonesr272020.converters.PostListConverter;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.CommunityNonExistentException;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UserBannedException;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.FullPostDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostListDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostUpdateDto;
import rs.ac.uns.ftn.redditclonesr272020.model.indexing.IndexPost;
import rs.ac.uns.ftn.redditclonesr272020.services.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    @Autowired
    private PdfService pdfService;

    Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    @Transactional
    public ResponseEntity<String> createPost(@Valid @RequestPart("json") PostDto postDto, BindingResult result, Authentication authentication, @RequestPart(value = "pdf", required = false) MultipartFile pdf) {
        // Initial checks to validate entity
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Post is not valid");
        if (postDto.getImagePath() == null && postDto.getText() == null)
            return ResponseEntity.badRequest().body("Post must have either text or image");

        var author = userService.findUserByUsername(authentication.getName());
        if (author.isEmpty()) return ResponseEntity.badRequest().body("User is not valid");

        var community = communityService.findCommunityByName(postDto.getCommunityName());
        if (community.isEmpty()) return ResponseEntity.badRequest().body("Community is not valid");

        Iterable<Flair> flairs = flairService.findAllById(postDto.getFlairs());
        Set<Flair> postFlairs = new HashSet<>();
        for (var flair : flairs) {
            postFlairs.add(flair);
        }

        Optional<String> pdfPath = Optional.empty();
        Optional<String> parsedText = Optional.empty();
        if (pdf != null) {
            pdfPath = pdfService.saveFile(pdf);
            parsedText = pdfService.parsePdf(pdf);
        }

        // Create the entity
        Post post = new Post();
        post.setFlairs(postFlairs);
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText() == null ? "" : postDto.getText());
        post.setImagePath(postDto.getImagePath());
        post.setUser(author.get());
        post.setCreationDate(LocalDate.now());
        pdfPath.ifPresent(post::setPdfPath);

        try {
            community.get().getPosts().add(post);
            communityService.update(community.get());

            var indexPostBuilder = IndexPost.builder().text(post.getText()).id(post.getId().toString()).title(post.getTitle());
            pdfPath.ifPresent(indexPostBuilder::postPDFPath);
            parsedText.ifPresent(indexPostBuilder::descriptionPDF);
            postService.index(indexPostBuilder.build());

            return ResponseEntity.ok().body(post.getId().toString());
        } catch (CommunityNonExistentException e) {
            return ResponseEntity.badRequest().body("Community not valid");
        } catch (UserBannedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is banned in this community");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePost(Authentication authentication, @PathVariable("id") String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID");
        }

        var post = postService.findById(uuid);
        if (post.isEmpty()) return ResponseEntity.badRequest().body("Post doesn't exist");

        String author = post.get().getUser().getUsername();
        if (!author.equals(authentication.getName()))
            return ResponseEntity.badRequest().body("Authenticated user is not the author of post.");

        postService.delete(post.get());
        if (post.get().getImagePath() != null && !post.get().getImagePath().isEmpty())
            imageService.delete(post.get().getImagePath());
//        communityService.findByName(post.get().)
        return ResponseEntity.ok().body("Deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") String id, Authentication authentication, @RequestBody PostUpdateDto postDto, BindingResult result) {
        // post is valid
        if (result.hasErrors()) return ResponseEntity.badRequest().body("Invalid JSON");

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid UUID");
        }

        var post = postService.findById(uuid);
        if (post.isEmpty()) return ResponseEntity.badRequest().body("Post not found");

        // current principal must be the author of post
        var author = post.get().getUser().getUsername();
        if (!authentication.getName().equals(author))
            return ResponseEntity.badRequest().body("Authenticated user is not the author of post.");

        //
        if (post.get().getImagePath() != null && !post.get().getImagePath().isEmpty() && !postDto.getText().isEmpty())
            return ResponseEntity.badRequest().body("Cannot update image post with text");

        var p = post.get();
        var flairs = new HashSet<Flair>();
        for (var f : flairService.findAllById(postDto.getFlairs()))
            flairs.add(f);

        p.setFlairs(flairs);
        p.setText(postDto.getText());

        postService.save(p);

        return ResponseEntity.ok("Post updated");
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<FullPostDto> findPostById(@PathVariable("id") String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        var post = postService.findById(uuid);
        if (post.isEmpty()) return ResponseEntity.notFound().build();

        var conv = new FullPostConverter();
        var dto = conv.toDto(post.get());

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<Iterable<PostListDto>> getAllPosts(Authentication auth, @RequestParam(value = "description", required = false) String description,
                                                             @RequestParam(value = "title", required = false) String title, @RequestParam(value = "text", required = false) String text) {
        var postListConverter = new PostListConverter();
        Iterable<Post> posts;
        if (description != null || title != null || text != null) {
            logger.info("Searching posts with query: title: {}, description: {}, text: {}", title, description, text);
            posts = postService.searchPosts(description, title, text);
        } else {
            posts = postService.getAllPosts();
        }
        var postDtos = new HashSet<PostListDto>();
        for (var post : posts) {
            var postDto = postListConverter.toDto(post);
            postDto.setKarma(reactionService.getPostKarma(post.getId()));
            postDto.setCommentCount(commentService.getCountByPostId(post.getId()));
            postDtos.add(postDto);

            if (auth != null) {
                var userReaction = reactionService.findByUserAndPost(auth.getName(), post.getId());
                userReaction.ifPresent(reaction -> postDto.setReaction(reaction.getReactionType()));
            }
        }
        return ResponseEntity.ok(postDtos);
    }
}
