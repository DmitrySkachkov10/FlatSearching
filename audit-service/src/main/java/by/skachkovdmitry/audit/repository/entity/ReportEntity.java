package by.skachkovdmitry.audit.repository.entity;

import by.skachkovdmitry.audit.core.dto.UserActionAuditParam;
import by.skachkovdmitry.audit.core.enums.ReportStatus;
import by.skachkovdmitry.audit.core.enums.ReportType;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private ReportType type;

    private String description;


    private String user;

    private String from;

    private String to;
}
