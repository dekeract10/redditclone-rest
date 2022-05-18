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
        return new PostListDto(
                p.getId(),
                p.getTitle(),
                p.getText(),
                p.getCreationDate(),
                p.getImagePath(),
                p.getFlairs().stream().map(Flair::getName).collect(Collectors.toSet()),
                p.getUser().getUsername(),
                p.getUser().getDisplayName()
                );
    }
}
