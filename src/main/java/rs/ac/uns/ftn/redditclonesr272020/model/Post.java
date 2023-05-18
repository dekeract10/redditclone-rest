package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {

    public Post() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    @Column(name = "pdf_path", nullable = true)
    private String pdfPath;


    @ManyToOne
    private Community community;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Flair> flairs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}