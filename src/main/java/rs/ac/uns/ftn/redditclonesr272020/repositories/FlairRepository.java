package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;

import java.util.UUID;

public interface FlairRepository extends JpaRepository<Flair, UUID> {
}
