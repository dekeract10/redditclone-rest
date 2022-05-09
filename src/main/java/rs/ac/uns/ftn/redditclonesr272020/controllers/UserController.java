package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UsernameTakenException;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.UserDto;
import rs.ac.uns.ftn.redditclonesr272020.security.TokenUtils;
import rs.ac.uns.ftn.redditclonesr272020.services.UserService;

import javax.validation.Valid;
import java.util.UUID;

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
    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @GetMapping()
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto user, BindingResult result) {
//        return ResponseEntity.ok("pera");
        logger.info("creating user with username : {}, email : {}", user.getUsername(), user.getEmail());
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().toString());
        }
        try {
            UUID uuid = userService.createUser(user);
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        User user = userService.findUserById(uuid);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
