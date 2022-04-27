package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
public class Comment {
    public Comment(){
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "comment_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "text")
    private String text;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "replies_to", nullable = true)
    private Comment repliesTo;

    @ManyToOne
    @JoinColumn(name = "post", nullable = false)
    private Post post;
}