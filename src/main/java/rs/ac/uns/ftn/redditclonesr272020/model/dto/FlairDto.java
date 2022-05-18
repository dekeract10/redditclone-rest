package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class FlairDto implements Serializable {
    private final UUID id;
    private final String name;
}
