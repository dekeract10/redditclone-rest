package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModeratorDto implements Serializable {
    private final String username;
    private final String displayName;
}
