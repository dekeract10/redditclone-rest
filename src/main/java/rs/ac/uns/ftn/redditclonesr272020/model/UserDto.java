package rs.ac.uns.ftn.redditclonesr272020.model;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    @Pattern(regexp = "^[A-Za-z0-9_]{3,20}$", message = "Username must be between 3 and 20 characters long and contain only letters and numbers and _")
    private String username;

    @Email
    private String email;

    @Length(min = 12, max = 128, message = "Password must be between 6 and 128 characters long")
    private String password;
}
