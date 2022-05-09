package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.CommunityNonExistentException;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostDto;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommunityRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.FlairRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.PostRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private FlairRepository flairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityService communityService;

    @Override
    public Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Iterable<Post> findPostsByCommunityId(UUID communityId) {
        var community = communityRepository.findById(communityId);
        if (community.isPresent()) {
            return community.get().getPosts();
        } else {
            return new HashSet<>();
        }
    }

    @Override
    @Transactional
    public Post save(PostDto postDto, String author) {
        Post post = new Post();

        // just in case something wonky happens, technically this should be impossible as the token was already verified
        var user = userRepository.findByUsername(author);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        Iterable<Flair> flairs = flairRepository.findAllById(postDto.getFlairs());
        Set<Flair> flairSet = new HashSet<>();
        for (var flair : flairs){
            flairSet.add(flair);
        }

        post.setFlairs(flairSet);
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setImagePath(postDto.getImagePath());
        post.setUser(user.get());
        post.setCreationDate(LocalDate.now());

        var community = communityRepository.findById(postDto.getCommunity());
        if (community.isEmpty()) {
            throw new CommunityNonExistentException("Community does not exist");
        }
        community.get().getPosts().add(post);
        communityRepository.save(community.get());

        return post;
    }
}
