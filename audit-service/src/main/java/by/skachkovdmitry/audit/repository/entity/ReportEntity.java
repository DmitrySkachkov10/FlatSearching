package by.skachkovdmitry.audit.repository.entity;

import by.skachkovdmitry.audit.core.enums.ReportStatus;
import by.skachkovdmitry.audit.core.enums.ReportType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "report", schema = "audit")
public class ReportEntity {
    @Id
    private UUID uuid;

    @Column(name = "dt_create")
    private LocalDateTime createDate;

    @Column(name = "dt_update")
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    private String description;

    private UUID userUuid;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;


    public ReportEntity() {
    }

    public ReportEntity(UUID uuid, LocalDateTime createDate, LocalDateTime updateDate, ReportStatus status, ReportType type, String description, UUID userUuid, LocalDateTime fromDate, LocalDateTime toDate) {
        this.uuid = uuid;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
        this.type = type;
        this.description = description;
        this.userUuid = userUuid;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportEntity that)) return false;
        return Objects.equals(uuid, that.uuid) && Objects.equals(createDate, that.createDate) && Objects.equals(updateDate, that.updateDate) && status == that.status && type == that.type && Objects.equals(description, that.description) && Objects.equals(userUuid, that.userUuid) && Objects.equals(fromDate, that.fromDate) && Objects.equals(toDate, that.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, createDate, updateDate, status, type, description, userUuid, fromDate, toDate);
    }
}
