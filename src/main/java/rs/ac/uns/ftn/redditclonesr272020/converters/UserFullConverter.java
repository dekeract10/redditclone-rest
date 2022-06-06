package rs.ac.uns.ftn.redditclonesr272020.converters;

import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.FullPostDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.UserFullDto;

public class UserFullConverter implements Converter<User, UserFullDto> {
    @Override
    public UserFullDto toDto(User u) {
        return new UserFullDto(
                u.getUsername(),
                u.getEmail(),
                u.getAvatar(),
                u.getRegistrationDate(),
                u.getDescription(),
                u.getDisplayName()
        );
    }
}
