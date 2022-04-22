package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UsernameTakenException;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.UserDto;
import rs.ac.uns.ftn.redditclonesr272020.services.UserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto user, BindingResult result) {
//        return ResponseEntity.ok("pera");
        logger.info("creating user with username : {}, email : {}", user.getUsername(), user.getEmail());
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().toString());
        }
        try {
            UUID uuid = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid.toString());
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

    @GetMapping()
    public ResponseEntity<User> findUser(@RequestParam("username") String username){
        User user = userService.findUserByUsername(username);
        if (user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
