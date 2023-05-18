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
import rs.ac.uns.ftn.redditclonesr272020.model.Post;
import rs.ac.uns.ftn.redditclonesr272020.model.indexing.IndexPost;
import rs.ac.uns.ftn.redditclonesr272020.repositories.CommunityRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.FlairRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.PostRepository;
import rs.ac.uns.ftn.redditclonesr272020.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private FlairRepository flairRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private PostIndexRepository postIndexRepository;

    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Iterable<Post> findPostsByCommunityId(UUID communityId) {
        var community = communityRepository.findById(communityId);
        if (community.isPresent()) {
            return community.get().getPosts();
        } else {
            return new HashSet<>();
        }
    }

    @Override
    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Iterable<Post> findPostsByCommunityName(String name) {
        var community = communityService.findByName(name);
        if (community.isEmpty())
            return new HashSet<>();
        else
            return community.get().getPosts();
    }

    @Override
    @Transactional
    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Iterable<Post> findPostsByUser(String username) {
        return postRepository.findAllByUserUsername(username);
    }

    @Override
    public void index(IndexPost indexPost) {
        postIndexRepository.save(indexPost);
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    @Transactional
    public Iterable<Post> searchPosts(String description, String title, String text) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (description != null) {
            query.must(QueryBuilders.matchQuery("descriptionPDF", description));
        }
        if (title != null) {
            query.must(QueryBuilders.matchQuery("title", title));
        }
        if (text != null) {
            query.must(QueryBuilders.matchQuery("text", text));
         }

        var searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();
        var indexPosts = elasticsearchRestTemplate.search(searchQuery, IndexPost.class, IndexCoordinates.of("posts_reddit"));

        logger.info("found: {}", indexPosts);


        return postRepository.findAllById( indexPosts.map(p -> UUID.fromString(p.getContent().getId())));
    }
}
