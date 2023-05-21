package rs.ac.uns.ftn.redditclonesr272020.model.indexing;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;

@Document(indexName = "communities_reddit")
@Setting(settingPath = "analyzers/serbianAnalyzer.json")
@Data
@Builder
public class IndexCommunity {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String postPDFPath;

    @Field(type = FieldType.Text)
    private String descriptionPDF;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text)
    private String description;
}
