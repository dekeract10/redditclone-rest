package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.redditclonesr272020.converters.PostListConverter;
import rs.ac.uns.ftn.redditclonesr272020.converters.UserFullConverter;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UsernameTakenException;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.*;
import rs.ac.uns.ftn.redditclonesr272020.security.TokenUtils;
import rs.ac.uns.ftn.redditclonesr272020.services.CommentService;
import rs.ac.uns.ftn.redditclonesr272020.services.PostService;
import rs.ac.uns.ftn.redditclonesr272020.services.ReactionService;
import rs.ac.uns.ftn.redditclonesr272020.services.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @GetMapping()
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto user, BindingResult result) {
        logger.info("creating user with username : {}, email : {}", user.getUsername(), user.getEmail());
        if (result.hasErrors()){
            var errorDto = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toSet());
            return ResponseEntity.badRequest().body(errorDto);
        }


        try {
            userService.createUser(user);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                return ResponseEntity.ok(tokenUtils.generateToken(userDetails));
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        } catch (UsernameTakenException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> putUser(
            @PathVariable("username") String username,
            @RequestBody @Valid UserUpdateDto updateDto,
            Authentication authentication,
            BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().build();

        if (!authentication.getName().equals(username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        var user = userService.findUserByUsername(username);
        if (user.isEmpty())
            return ResponseEntity.notFound().build();

        logger.info("updating user {}", updateDto);
        if (updateDto.getAvatar() != null && !updateDto.getAvatar().isBlank())
            user.get().setAvatar(updateDto.getAvatar());
        if (updateDto.getDescription() != null)
            user.get().setDescription(updateDto.getDescription());
        if (updateDto.getDisplayName() != null)
            user.get().setDisplayName(updateDto.getDisplayName());

        try{
            userService.update(user.get());
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserFullDto> getUser(@PathVariable("username") String username) {
        var user = userService.findUserByUsername(username );
        if (user.isEmpty()) {return ResponseEntity.notFound().build();}

        var conv = new UserFullConverter();
        var userFullDto = conv.toDto(user.get());
        return ResponseEntity.ok(userFullDto);

    }

    @PutMapping("{username}/password")
    public ResponseEntity<String> changePassword(@PathVariable("username") String username,
                                                 @RequestBody @Valid UserPasswordDto userPasswordDto,
                                                 BindingResult result,
                                                 Authentication auth) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body("Password must be longer than 12 characters");
        var user = userService.findUserByUsername(username);
        if (user.isEmpty())
            return ResponseEntity.badRequest().build();

        if (username.equals(user.get().getUsername())) {
            user.get().setPassword(passwordEncoder.encode(userPasswordDto.getPassword()));
            userService.update(user.get());
            var userDetails = userDetailsService.loadUserByUsername(auth.getName());
            return ResponseEntity.ok(tokenUtils.generateToken(userDetails));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("{username}/posts")
    @Transactional
    public ResponseEntity<Iterable<PostListDto>> getPosts(@PathVariable("username") String username){
        var conv = new PostListConverter();
        var posts = postService.findPostsByUser(username);
        var postDtos = new ArrayList<PostListDto>();
        for (var post : posts){
            var postDto = conv.toDto(post);
            postDto.setCommentCount(commentService.getCountByPostId(postDto.getId()));
            postDto.setKarma(reactionService.getPostKarma(postDto.getId()));
            postDtos.add(postDto);
        }
        return ResponseEntity.ok(postDtos);
    }
}
