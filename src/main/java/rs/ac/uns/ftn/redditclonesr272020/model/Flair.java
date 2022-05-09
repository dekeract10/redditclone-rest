package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "flair")
@Getter
@Setter
public class Flair implements Serializable {
    public Flair() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flairs")
    private Set<Community> communities = new HashSet<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flair)) return false;
        Flair flair = (Flair) o;
        return id.equals(flair.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}