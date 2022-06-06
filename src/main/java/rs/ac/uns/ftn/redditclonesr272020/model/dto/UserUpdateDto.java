package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class UserUpdateDto implements Serializable {
//    private final String password;
    private final String avatar;
    @Length(max = 100)
    private final String description;
    @Length(max = 20)
    private final String displayName;
}
