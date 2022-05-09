package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "banned")
@Getter
@Setter
@AllArgsConstructor
public class Banned implements Serializable {
    public Banned() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Banned)) return false;
        Banned banned = (Banned) o;
        return id.equals(banned.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}