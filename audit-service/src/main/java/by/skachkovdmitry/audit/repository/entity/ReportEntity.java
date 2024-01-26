package by.skachkovdmitry.audit.repository.entity;

import by.skachkovdmitry.audit.core.dto.UserActionAuditParam;
import by.skachkovdmitry.audit.core.enums.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report", schema = "audit")
public class ReportEntity {
    @Id
    private UUID uuid;

    @Column(name = "dt_create")
    private LocalDateTime createDate;

    @Column(name = "dt_update")
    private LocalDateTime updateDate;

    private ReportStatus status;

    private final String type = "JOURNAL_AUDIT";

    private String description;

    private UserActionAuditParam param;
}
