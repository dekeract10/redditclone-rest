package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "reaction", uniqueConstraints = { @UniqueConstraint(columnNames = { "user", "post" })})
@Getter
@Setter
public class Reaction {
    public Reaction(){
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reaction_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post", nullable = false)
    private Post post;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}