package rs.ac.uns.ftn.redditclonesr272020.converters;

import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.PostListDto;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

public class PostListConverter implements Converter<Post, PostListDto> {
    @Override
    @Transactional
    public PostListDto toDto(Post p) {
        return PostListDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .text(p.getText())
                .creationDate(p.getCreationDate())
                .imagePath(p.getImagePath())
                .pdfPath(p.getPdfPath())
                .flairNames(p.getFlairs().stream().map(Flair::getName).collect(Collectors.toSet()))
                .userUsername(p.getUser().getUsername())
                .userDisplayName(p.getUser().getDisplayName())
                .communityName(p.getCommunity().getName())
                .pdfName(p.getPdfName())
                .build();
    }
}
