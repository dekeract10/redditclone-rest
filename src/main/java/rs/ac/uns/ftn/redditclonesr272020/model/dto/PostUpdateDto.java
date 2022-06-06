package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class PostUpdateDto implements Serializable {
    private final String text;
    private final List<UUID> flairs;
}
