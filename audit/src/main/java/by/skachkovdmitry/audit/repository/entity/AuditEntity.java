package by.skachkovdmitry.audit.repository.entity;

import by.skachkovdmitry.audit.core.EssenceType;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "audit", name = "audit")
public class AuditEntity {

    @Id
    private UUID uuid;

    @Column(name = "dt_create")
    private long dtCreate;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private UserEntity user;

    private String text;

    @Enumerated(EnumType.STRING)
    private EssenceType essenceType;

    private String id;


    public AuditEntity() {
    }

    public AuditEntity(UUID uuid, long dtCreate, UserEntity user, String text, EssenceType essenceType, String id) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.user = user;
        this.text = text;
        this.essenceType = essenceType;
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getDt_create() {
        return dtCreate;
    }

    public void setDt_create(long dtCreate) {
        this.dtCreate = dtCreate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EssenceType getEssenceType() {
        return essenceType;
    }

    public void setEssenceType(EssenceType essenceType) {
        this.essenceType = essenceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof AuditEntity that)) return false;
        return dtCreate == that.dtCreate && Objects.equals(uuid, that.uuid)  && Objects.equals(text, that.text) && essenceType == that.essenceType && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, dtCreate, text, essenceType, id);
    }
}
