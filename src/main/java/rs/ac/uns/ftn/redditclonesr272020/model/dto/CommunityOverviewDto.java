package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Data
@Builder
public class CommunityOverviewDto implements Serializable {
    private final UUID id;
    private final Set<ModeratorDto> moderators;
    private final Set<String> flairNames;
    private final String name;
    private final String description;
    private final LocalDate creationDate;
    private final List<String> ruleTexts;
    private final boolean isSuspended;
    private final String suspensionReason;
    private String pdfPath;
    private String pdfName;
}
