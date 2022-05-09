package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.CommunityNonExistentException;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostDto;
import rs.ac.uns.ftn.redditclonesr272020.services.PostService;
import rs.ac.uns.ftn.redditclonesr272020.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MOD"})
    public ResponseEntity<String> createPost(@Valid @RequestBody PostDto postDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body("Post is not valid");

        var user = userService.findUserByUsername(authentication.getName());
        if (user.isEmpty())
            return ResponseEntity.badRequest().body("User is not valid");

        if (user.get().isBannedInCommunity(postDto.getCommunity()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is banned in this community");

        try {
            var post = postService.save(postDto, authentication.getName());
            return ResponseEntity.ok().body(post.getId().toString());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User not valid");
        } catch (CommunityNonExistentException e) {
            return ResponseEntity.badRequest().body("Community not valid");
        }
    }
}
