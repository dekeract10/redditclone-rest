package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rule")
@Getter
@Setter
@AllArgsConstructor
public class Rule {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    public Rule() {
        super();
        this.id = MyIdGenerator.generateId();
    }
}