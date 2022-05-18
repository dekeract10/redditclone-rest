package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class PostListDto implements Serializable {
    private final UUID id;
    private final String title;
    private final String text;
    private final LocalDate creationDate;
    private final String imagePath;
    private final Set<String> flairNames;
    private final String userUsername;
    private final String userDisplayName;
    private int commentCount;
    private int karma;
}
