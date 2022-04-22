package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value = "user")
@Getter
@Setter
@AllArgsConstructor
public class User {
    public User() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = true)
    private String email;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<Comment> comments = new java.util.LinkedHashSet<>();

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "display_name")
    private String displayName;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<Reaction> reactions = new java.util.LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<Post> posts = new java.util.LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<Banned> bans = new java.util.LinkedHashSet<>();

//    public Set<Banned> getBans() {
//        return bans;
//    }
//
//    public void setBans(Set<Banned> bans) {
//        this.bans = bans;
//    }
}