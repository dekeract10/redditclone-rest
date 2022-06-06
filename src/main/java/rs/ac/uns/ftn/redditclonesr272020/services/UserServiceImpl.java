package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.UsernameTakenException;
import rs.ac.uns.ftn.redditclonesr272020.model.Moderator;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.UserDto;
import rs.ac.uns.ftn.redditclonesr272020.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UUID createUser(UserDto user) throws UsernameTakenException {
        if (userRepository.existsUserByUsername(user.getUsername()))
            throw new UsernameTakenException();

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRegistrationDate(LocalDate.now());

        userRepository.save(newUser);

        return newUser.getId();
    }

    @Override
    public User findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }
    @PersistenceContext EntityManager em;

    @Override
    public Moderator makeModerator(String username){
        em.createNativeQuery("UPDATE user u SET TYPE = ?" +
            " WHERE u.username LIKE ?")
            .setParameter(1, "mod")
                .setParameter(2, username)
                .executeUpdate();
        return (Moderator)userRepository.findByUsername(username).get();
    }
}
