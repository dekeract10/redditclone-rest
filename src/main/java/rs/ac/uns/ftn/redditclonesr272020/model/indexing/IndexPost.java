package rs.ac.uns.ftn.redditclonesr272020.model.indexing;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;

@Document(indexName = "posts_reddit")
@Setting(settingPath = "analyzers/serbianAnalyzer.json")
@Data
@Builder
public class IndexPost {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String postPDFPath;

    @Field(type = FieldType.Text)
    private String descriptionPDF;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String text;
}
