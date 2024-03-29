package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "community")
@Getter
@Setter
@AllArgsConstructor
public class Community implements Serializable {
    public Community() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToMany(cascade = {CascadeType.MERGE} , fetch = FetchType.LAZY)
    @JoinTable(name = "moderates", joinColumns = @JoinColumn(name = "community_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Moderator> moderators = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "flair_community", joinColumns = @JoinColumn(name = "community_id"), inverseJoinColumns = @JoinColumn(name = "flair_id"))
    private Set<Flair> flairs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "community_id")
    private Set<Post> posts = new HashSet<>();


    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private List<Rule> rules = new ArrayList<>();

    @Column(name = "is_suspended", nullable = false)
    private boolean isSuspended;

    @Column(name = "suspension_reason", nullable = true)
    private String suspensionReason;

    @Column(name = "pdf_path", nullable = true)
    private String pdfPath;

    @Column(name = "pdf_name", nullable = true)
    private String pdfName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Community)) return false;
        Community community = (Community) o;
        return id.equals(community.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}