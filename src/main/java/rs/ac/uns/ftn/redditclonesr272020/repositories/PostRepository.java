package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;

import java.util.UUID;

public interface PostRepository extends CrudRepository<Post, UUID> {
}
