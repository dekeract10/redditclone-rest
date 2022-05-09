package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;

import java.util.UUID;

public interface FlairRepository extends CrudRepository<Flair, UUID> {
}
