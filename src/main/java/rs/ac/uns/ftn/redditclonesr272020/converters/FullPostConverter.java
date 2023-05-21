package rs.ac.uns.ftn.redditclonesr272020.converters;

import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.FlairDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.FullPostDto;

import java.util.stream.Collectors;

public class FullPostConverter implements Converter<Post, FullPostDto> {
    @Override
    public FullPostDto toDto(Post model) {
        return FullPostDto.builder().id(model.getId()).title(model.getTitle()).text(model.getText()).creationDate(model.getCreationDate()).imagePath(model.getImagePath()).flairs(model.getFlairs().stream().map(f -> new FlairDto(f.getId(), f.getName())).collect(Collectors.toSet())).userId(model.getUser().getId()).userUsername(model.getUser().getUsername()).userAvatar(model.getUser().getAvatar()).userDisplayName(model.getUser().getDisplayName()).pdfPath(model.getPdfPath()).pdfName(model.getPdfName()).build();
    }
}
