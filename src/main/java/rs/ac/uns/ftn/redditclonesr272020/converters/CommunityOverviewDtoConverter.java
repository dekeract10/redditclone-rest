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
        return CommunityOverviewDto.builder().id(community.getId()).moderators(community.getModerators().stream().map(moderator -> new ModeratorDto(moderator.getUsername(), moderator.getDisplayName())).collect(Collectors.toSet())).flairNames(community.getFlairs().stream().map(Flair::getName).collect(Collectors.toSet())).name(community.getName()).description(community.getDescription()).creationDate(community.getCreationDate()).ruleTexts(community.getRules().stream().map(Rule::getText).collect(Collectors.toList())).isSuspended(community.isSuspended()).suspensionReason(community.getSuspensionReason()).pdfPath(community.getPdfPath()).pdfName(community.getPdfName()).build();
    }
}
