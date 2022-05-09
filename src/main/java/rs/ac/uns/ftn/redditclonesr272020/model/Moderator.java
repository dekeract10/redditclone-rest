package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("mod")
@Getter
@Setter
public class Moderator extends User {
    @Override
    public GrantedAuthority getRole() {
        return new SimpleGrantedAuthority("ROLE_MOD");
    }
}