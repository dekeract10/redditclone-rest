package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.repositories.FlairRepository;

import java.util.List;
import java.util.UUID;

@Service
public class FlairServiceImpl implements FlairService {
    @Autowired
    private FlairRepository flairRepository;

    @Override
    public Iterable<Flair> findAllById(List<UUID> flairs) {
        return flairRepository.findAllById(flairs);
    }
}
