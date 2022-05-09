package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @Column(name = "text", nullable = false)
    private String text;

    public Rule() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    public Rule(String text){
        super();
        this.id = MyIdGenerator.generateId();
        this.text = text;
    }
}