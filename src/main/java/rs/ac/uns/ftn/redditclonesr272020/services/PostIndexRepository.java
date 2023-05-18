package rs.ac.uns.ftn.redditclonesr272020.services;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.redditclonesr272020.model.indexing.IndexPost;

public interface PostIndexRepository extends ElasticsearchRepository<IndexPost, String>{
    public SearchHits<IndexPost> findAllByDescriptionPDF(String description);
    public SearchHits<IndexPost> findAllByText(String text);
    public SearchHits<IndexPost> findAllByTitle(String title);
}
