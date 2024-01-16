package by.skachkovdmitry.personal_account.repo.entity;

import by.skachkovdmitry.personal_account.core.role.Roles;
import by.skachkovdmitry.personal_account.core.status.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "users", name = "users")
public class UserEntity {

    @Id
    private UUID uuid;

    @Column(name = "create_date")
    private LocalDateTime dtCreate;

    @Column(name = "update_date")
    @Version
    private LocalDateTime dtUpdate;
    private String fio;
    @Column(unique = true)
    private String mail;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String password;


    public UserEntity() {
    }

    public UserEntity(UUID uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate, String fio, String mail, Roles role, Status status, String password, boolean verified) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.fio = fio;
        this.mail = mail;
        this.role = role;
        this.status = status;
        this.password = password;
    }


    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity user)) return false;
        return dtCreate == user.dtCreate && dtUpdate == user.dtUpdate && Objects.equals(uuid, user.uuid) && Objects.equals(fio, user.fio) && Objects.equals(mail, user.mail) && Objects.equals(role, user.role) && status == user.status && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, dtCreate, dtUpdate, fio, mail, role, status, password);
    }
}
