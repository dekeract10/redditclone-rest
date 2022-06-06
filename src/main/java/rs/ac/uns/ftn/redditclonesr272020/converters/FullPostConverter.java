package rs.ac.uns.ftn.redditclonesr272020.converters;

import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.FlairDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.FullPostDto;

import java.util.stream.Collectors;

public class FullPostConverter implements Converter<Post, FullPostDto> {
    @Override
    public FullPostDto toDto(Post model) {
        return new FullPostDto(
                model.getId(),
                model.getTitle(),
                model.getText(),
                model.getCreationDate(),
                model.getImagePath(),
                model.getFlairs().stream().map(f -> new FlairDto(f.getId(), f.getName())).collect(Collectors.toSet()),
                model.getUser().getId(),
                model.getUser().getUsername(),
                model.getUser().getAvatar(),
                model.getUser().getDisplayName()
        );
    }
}
