package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CommentService {
    int getCountByPostId(UUID postId);
}
