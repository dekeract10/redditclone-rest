package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;
import rs.ac.uns.ftn.redditclonesr272020.model.ReactionType;

import java.io.Serializable;
import java.util.UUID;

@Data
public class CreateReactionDto implements Serializable {
    private final ReactionType reactionType;
    private final UUID postId;
    private final UUID commentId;
}
