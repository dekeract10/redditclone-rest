package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto implements Serializable {
    @NonNull
    private final String title;
    @NonNull
    private final String text;
    @NonNull
    private final String imagePath;
    @NonNull
    private final List<UUID> flairs;
    @NonNull
    private final UUID community;
}
