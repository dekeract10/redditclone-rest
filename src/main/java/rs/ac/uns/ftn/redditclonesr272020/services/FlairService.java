package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;

import java.util.List;
import java.util.UUID;

@Service
public interface FlairService {
    Iterable<Flair> findAllById(List<UUID> flairs);
}
