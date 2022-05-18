package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.exceptions.CommunityNonExistentException;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommunityRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.FlairRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.PostRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.UserRepository;

import javax.transaction.Transactional;
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
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Iterable<Post> findPostsByCommunityName(String name) {
        var community = communityService.findByName(name);
        if (community.isEmpty())
            return new HashSet<>();
        else
            return community.get().getPosts();
    }
}
