package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserFullDto implements Serializable {
    private final String username;
    private final String email;
    private final String avatar;
    private final LocalDate registrationDate;
    private final String description;
    private final String displayName;
    private int karma;
}
