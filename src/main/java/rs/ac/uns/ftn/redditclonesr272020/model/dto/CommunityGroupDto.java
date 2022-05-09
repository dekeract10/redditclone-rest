package rs.ac.uns.ftn.redditclonesr272020.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityGroupDto implements Serializable {
    private UUID id;
    private Set<String> moderatorUsernames;
    private Set<UUID> flairIds;
    private String name;
    private String description;
    private LocalDate creationDate;
    private List<UUID> ruleIds;
    private boolean isSuspended;
    private String suspensionReason;
}
