package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
public class CommunityDto implements Serializable {
    @NonNull
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "Community name must be between 3 and 20 characters long and can contain only letters and numbers")
    private final String name;
    @NonNull
    private final String description;
    @NonNull
    private final List<String> rules;
}
