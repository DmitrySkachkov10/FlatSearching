package by.skachkovdmitry.audit.repository.entity;

import by.skachkovdmitry.audit.core.enums.Roles;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "audit", name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_uuid")
    private UUID uuid;

    private String mail;

    private String fio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Roles role;


    @OneToMany(mappedBy = "user")
    @Transient
    private List<AuditEntity> audits;
    public UserEntity() {
    }

    public UserEntity(UUID uuid, String mail, String fio, Roles role) {
        this.uuid = uuid;
        this.mail = mail;
        this.fio = fio;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(uuid, that.uuid) && Objects.equals(mail, that.mail) && Objects.equals(fio, that.fio) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, mail, fio, role);
    }
}
