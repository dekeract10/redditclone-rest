package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Rule;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityDto;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityGroupDto;
import rs.ac.uns.ftn.redditclonesr272020.services.CommunityService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @GetMapping()
    @Secured({"ROLE_USER", "ROLE_MOD", "ROLE_ADMIN"})
    @Transactional
    public ResponseEntity<Iterable<CommunityGroupDto>> getAllCommunities() {
        var communities = communityService.findAll();
        var communitiesDto = new ArrayList<CommunityGroupDto>();
        for (var community : communities) {
            var communityDto = new CommunityGroupDto();
            communityDto.setId(community.getId());
            communityDto.setName(community.getName());
            communityDto.setDescription(community.getDescription());
            communityDto.setCreationDate(community.getCreationDate());
            communityDto.setModeratorUsernames(community.getModerators().stream().map(User::getUsername).collect(Collectors.toSet()));
            communityDto.setFlairIds(community.getFlairs().stream().map(Flair::getId).collect(Collectors.toSet()));
            communityDto.setSuspended(community.isSuspended());
            communityDto.setSuspensionReason(community.getSuspensionReason());
            communityDto.setRuleIds(community.getRules().stream().map(Rule::getId).collect(Collectors.toList()));
            communitiesDto.add(communityDto);
        }
        return ResponseEntity.ok(communitiesDto);
    }

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_MOD"})
    public ResponseEntity<String> createCommunity(@Valid @RequestBody CommunityDto communityDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().toString());

        try {
            var community = communityService.createCommunity(communityDto, authentication.getName());
            return ResponseEntity.ok().body(community.getId().toString());
        } catch (UsernameNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
