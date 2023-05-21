package rs.ac.uns.ftn.redditclonesr272020.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.indexing.IndexCommunity;

public interface CommunityIndexRepository extends ElasticsearchRepository<IndexCommunity, String> {
}
