package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "flair")
@Getter
@Setter
public class Flair {
    public Flair() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;
}