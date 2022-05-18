package rs.ac.uns.ftn.redditclonesr272020.converters;

import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Rule;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityOverviewDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.ModeratorDto;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

public class CommunityOverviewDtoConverter implements Converter<Community, CommunityOverviewDto> {
    @Override
    public CommunityOverviewDto toDto(Community community) {
        return new CommunityOverviewDto(
                community.getId(),
                community.getModerators().stream().map(moderator -> new ModeratorDto(moderator.getUsername(), moderator.getDisplayName())).collect(Collectors.toSet()),
                community.getFlairs().stream().map(Flair::getName).collect(Collectors.toSet()),
                community.getName(),
                community.getDescription(),
                community.getCreationDate(),
                community.getRules().stream().map(Rule::getText).collect(Collectors.toList()),
                community.isSuspended(),
                community.getSuspensionReason()
                );
    }
}
