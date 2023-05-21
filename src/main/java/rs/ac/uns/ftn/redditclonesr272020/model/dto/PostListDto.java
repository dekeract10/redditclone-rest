package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Builder;
import lombok.Data;
import rs.ac.uns.ftn.redditclonesr272020.model.ReactionType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class PostListDto implements Serializable {
    private final UUID id;
    private final String title;
    private final String text;
    private final LocalDate creationDate;
    private final String imagePath;
    private final String pdfPath;
    private final String pdfName;
    private final Set<String> flairNames;
    private final String userUsername;
    private final String userDisplayName;
    private final String communityName;
    private int commentCount;
    private int karma;
    private ReactionType reaction;
}
