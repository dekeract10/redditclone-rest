package rs.ac.uns.ftn.redditclonesr272020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.redditclonesr272020.converters.CommunityOverviewDtoConverter;
import rs.ac.uns.ftn.redditclonesr272020.converters.PostListConverter;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Flair;
import rs.ac.uns.ftn.redditclonesr272020.model.Rule;
import rs.ac.uns.ftn.redditclonesr272020.model.User;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.*;
import rs.ac.uns.ftn.redditclonesr272020.security.TokenUtils;
import rs.ac.uns.ftn.redditclonesr272020.services.CommentService;
import rs.ac.uns.ftn.redditclonesr272020.services.CommunityService;
import rs.ac.uns.ftn.redditclonesr272020.services.PostService;
import rs.ac.uns.ftn.redditclonesr272020.services.ReactionService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PostService postService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping()
    @Secured({"ROLE_USER", "ROLE_MOD", "ROLE_ADMIN"})
    @Transactional
    public ResponseEntity<Iterable<CommunityGroupDto>> getAllCommunities(@RequestParam(value = "description",
            required = false) String description, @RequestParam(value = "name", required = false) String name,
                                                                         @RequestParam(value = "descriptionPDF",
                                                                                 required = false) String descriptionPDF,
                                                                         @RequestParam(value = "min_posts", required = false) Integer minPosts,
                                                                         @RequestParam(value = "max_posts", required = false) Integer maxPosts) {
        Iterable<Community> communities;
        if (description != null || name != null || descriptionPDF != null || minPosts != null || maxPosts != null ) {
            communities = communityService.search(description, name, descriptionPDF, minPosts, maxPosts);
        } else {
            communities = communityService.findAll();
        }
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
            communityDto.setAverageKarma(community.getPosts().stream().mapToDouble(p -> reactionService.getPostKarma(p.getId())).average().orElse(0));
            communityDto.setPdfPath(community.getPdfPath());
            communityDto.setPdfName(community.getPdfName());

            communitiesDto.add(communityDto);
        }
        return ResponseEntity.ok(communitiesDto);
    }

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_MOD", "ROLE_ADMIN"})
    public ResponseEntity<String> createCommunity(@Valid @RequestPart("json") CommunityDto communityDto,
                                                  BindingResult result, Authentication authentication,
                                                  @RequestPart(value = "pdf", required = false) MultipartFile pdf) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors().toString());

        try {
            var community = communityService.createCommunity(communityDto, authentication.getName(), pdf);

            return ResponseEntity.ok().body(community.getId().toString());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User is not valid");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{name}")
    @Transactional
    public ResponseEntity<CommunityOverviewDto> getCommunity(@PathVariable String name) {
        var communityOpt = communityService.findCommunityByName(name);
        if (communityOpt.isEmpty()) return ResponseEntity.notFound().build();

        var communityConverter = new CommunityOverviewDtoConverter();
        var community = communityOpt.get();
        var communityDto = communityConverter.toDto(community);

        return ResponseEntity.ok(communityDto);
    }

    @GetMapping("/{name}/posts")
    @Transactional
    public ResponseEntity<Iterable<PostListDto>> getCommunityPosts(@PathVariable String name, Authentication auth) {
        var posts = postService.findPostsByCommunityName(name);
        var postListConverter = new PostListConverter();
        var postDtos = new ArrayList<PostListDto>();
        for (var post : posts) {
            var postDto = postListConverter.toDto(post);

            var commentCount = commentService.getCountByPostId(post.getId());
            postDto.setCommentCount(commentCount);

            int karma = reactionService.getPostKarma(postDto.getId());
            postDto.setKarma(karma);

            if (auth != null) {
                var userReaction = reactionService.findByUserAndPost(auth.getName(), post.getId());
                userReaction.ifPresent(reaction -> postDto.setReaction(reaction.getReactionType()));
            }

            postDtos.add(postDto);
        }
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("{name}/flairs")
    @Transactional
    public ResponseEntity<List<FlairDto>> getCommunityFlairs(@PathVariable String name) {
        var community = communityService.findCommunityByName(name);
        if (community.isEmpty()) return ResponseEntity.notFound().build();

        var flairDtos =
                community.get().getFlairs().stream().map(f -> new FlairDto(f.getId(), f.getName())).collect(Collectors.toList());

        return ResponseEntity.ok(flairDtos);
    }
}
