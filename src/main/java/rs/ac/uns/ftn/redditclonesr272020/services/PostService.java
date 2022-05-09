package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostDto;

import java.util.List;
import java.util.UUID;

@Service
public interface PostService {
    Iterable<Post> getAllPosts();
    Iterable<Post> findPostsByCommunityId(UUID communityId);
    Post save(PostDto postDto, String username);
}
