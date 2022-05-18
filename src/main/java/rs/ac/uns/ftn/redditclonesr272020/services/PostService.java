package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;

import java.util.UUID;

@Service
public interface PostService {
    Iterable<Post> getAllPosts();
    Iterable<Post> findPostsByCommunityId(UUID communityId);
    Post save(Post post);

    Iterable<Post> findPostsByCommunityName(String name);

}
