package rs.ac.uns.ftn.redditclonesr272020.security;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.repositories.UserRepository;

@Service
@Primary
//@Qualifier("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

//    @PostConstruct
//    public void completeSetup(){
//        userRepository = context.getBean(UserRepository.class);
//    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(MyUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isPresent())
            return new MyUserDetails(user.get());
        else
            throw new UsernameNotFoundException("Username not found");
    }
}
