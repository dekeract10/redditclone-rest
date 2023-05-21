package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.indexing.IndexPost;

public interface PostIndexRepository extends ElasticsearchRepository<IndexPost, String>{
}
