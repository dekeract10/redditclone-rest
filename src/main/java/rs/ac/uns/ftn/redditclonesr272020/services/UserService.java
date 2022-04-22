package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UsernameTakenException;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.UserDto;

import java.util.UUID;

@Service
public interface UserService {
    public void saveUser(User user);

    public User findUserByUsername(String username);

    public UUID createUser(UserDto user) throws UsernameTakenException;

    public User findUserById(UUID id);
}
