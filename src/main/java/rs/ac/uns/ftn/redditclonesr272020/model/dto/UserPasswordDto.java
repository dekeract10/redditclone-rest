package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class UserPasswordDto implements Serializable {
    @Length(min = 12)
    private String password;
}
