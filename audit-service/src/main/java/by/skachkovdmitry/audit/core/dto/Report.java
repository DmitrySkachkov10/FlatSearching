package by.skachkovdmitry.audit.core.dto;

import by.skachkovdmitry.audit.core.enums.ReportStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Report {

    private String uuid;

    private LocalDateTime dt_create;

    private LocalDateTime dt_update;

    private ReportStatus status;

    private final String type = "JOURNAL_AUDIT";

    private String description;

    private UserActionAuditParam param;

}
