package rs.ac.uns.ftn.redditclonesr272020.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("adm")
@Getter
@Setter
public class Administrator extends User {
    @Override
    public GrantedAuthority getRole() {
        return new SimpleGrantedAuthority("ROLE_ADMIN");
    }
}