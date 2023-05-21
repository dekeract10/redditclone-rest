package rs.ac.uns.ftn.redditclonesr272020.services;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.redditclonesr272020.model.Community;
import rs.ac.uns.ftn.redditclonesr272020.model.Moderator;
import rs.ac.uns.ftn.redditclonesr272020.model.Rule;
import rs.ac.uns.ftn.redditclonesr272020.model.dto.CommunityDto;
import rs.ac.uns.ftn.redditclonesr272020.model.indexing.IndexCommunity;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommunityIndexRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommunityRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Community save(Community community) {
        return communityRepository.save(community);
    }

    @Override
    @Transactional
    public Optional<Community> findById(UUID id) {
        return communityRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Community> findByName(String communityName) {
        return communityRepository.findByName(communityName);
    }

    @Autowired
    private PdfService pdfService;

    @Autowired
    private CommunityIndexRepository communityIndexRepository;

    @Override
    @Transactional
    public Community createCommunity(CommunityDto communityDto, String username, MultipartFile pdf) {
        var rules = new ArrayList<Rule>();
        for (var rule : communityDto.getRules()) {
            rules.add(new Rule(rule));
        }

        Set<Moderator> moderators = new HashSet<>();
        var newModerator = userService.makeModerator(username);
        moderators.add(newModerator);

        Optional<String> pdfPath = Optional.empty();
        Optional<String> parsedText = Optional.empty();
        if (pdf != null) {
            pdfPath = pdfService.saveFile(pdf);
            parsedText = pdfService.parsePdf(pdf);
        }


        var community = new Community();
        community.setCreationDate(LocalDate.now());
        community.setName(communityDto.getName());
        community.setDescription(communityDto.getDescription());
        community.setModerators(moderators);
        community.setRules(rules);
        if (pdf != null){
            community.setPdfName(pdf.getOriginalFilename());
            pdfPath.ifPresent(community::setPdfPath);
        }


        var indexPostBuilder = IndexCommunity.builder().description(community.getDescription()).id(community.getId().toString()).name(community.getName());
        pdfPath.ifPresent(indexPostBuilder::postPDFPath);
        parsedText.ifPresent(indexPostBuilder::descriptionPDF);

        communityRepository.save(community);
        communityIndexRepository.save(indexPostBuilder.build());
        return community;
    }

    @Override
    @Transactional
    public Iterable<Community> findAll() {
        return communityRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Community> findCommunityByName(String communityName) {
        return communityRepository.findByName(communityName);
    }

    @Override
    @Transactional
    public Community update(Community community) {
        return communityRepository.save(community);
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    private final Logger logger = LoggerFactory.getLogger(CommunityServiceImpl.class);

    @Override
    public Iterable<Community> search(String description, String name, String descriptionPDF, Integer minPosts, Integer maxPosts) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (description != null) {
            query.must(QueryBuilders.matchQuery("description", description));
        }
        if (name != null) {
            query.must(QueryBuilders.matchQuery("name", name));
        }
        if (descriptionPDF != null) {
            query.must(QueryBuilders.matchQuery("descriptionPDF", descriptionPDF));
        }

        var searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
        var indexCommunities = elasticsearchRestTemplate.search(searchQuery, IndexCommunity.class, IndexCoordinates.of("communities_reddit"));
        logger.info("found communities: {}", indexCommunities);
        return communityRepository.findAllById(indexCommunities.map(p -> UUID.fromString(p.getContent().getId()))).stream().filter(c -> postsBetween(c, minPosts, maxPosts)).collect(Collectors.toList());
    }

    private boolean postsBetween(Community community, Integer minPosts, Integer maxPosts) {
        return (minPosts == null || community.getPosts().size() > minPosts) && (maxPosts == null || community.getPosts().size() < maxPosts );
    }
}
