package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.Banner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import rs.ac.uns.ftn.redditclonesr272020.configuration.MyIdGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value = "user")
@Getter
@Setter
@AllArgsConstructor
public class User implements Serializable {
    public User() {
        super();
        this.id = MyIdGenerator.generateId();
    }

    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Banned> bans = new HashSet<>();

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = true)
    private String email;
    @Column(name = "avatar")
    private String avatar;


    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "display_name")
    private String displayName;

    public GrantedAuthority getRole() {
        return new SimpleGrantedAuthority("ROLE_USER");
    }

    public boolean isBannedInCommunity(UUID communityId){
        return this.getBans().stream().anyMatch(ban -> ban.getCommunity().getId().equals(communityId));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Moderator toModerator() {
        Moderator moderator = new Moderator();
        moderator.setId(this.getId());
        moderator.setEmail(this.getEmail());
        moderator.setUsername(this.getUsername());
        moderator.setPassword(this.getPassword());
        moderator.setAvatar(this.getAvatar());
        moderator.setRegistrationDate(this.getRegistrationDate());
        moderator.setDescription(this.getDescription());
        moderator.setDisplayName(this.getDisplayName());
        moderator.setBans(this.getBans());

        return moderator;
    }
}