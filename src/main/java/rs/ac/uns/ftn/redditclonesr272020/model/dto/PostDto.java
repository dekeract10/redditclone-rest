package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto implements Serializable {
    @NonNull
    @Length(min = 5, max = 100)
    private final String title;
    private final String text;
    private final String imagePath;
    @NonNull
    private final List<UUID> flairs;
    @NonNull
    private final String communityName;
}
