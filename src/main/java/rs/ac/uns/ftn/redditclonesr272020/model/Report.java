package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "report")
@Getter
@Setter
public class Report {
    public Report() {
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "report_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "reason")
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @Column(name = "accepted")
    private boolean accepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment", nullable = true)
    public Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post", nullable = true)
    public Post post;
}