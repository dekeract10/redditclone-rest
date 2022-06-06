package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Iterable<Post> findAllByUserUsername(String username);
}
