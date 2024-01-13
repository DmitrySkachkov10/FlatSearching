package by.skachkovdmitry.mailing_service.repo.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "users", name = "mail_queue")
public class MailVerifyEntity {

    @Id
    private UUID uuid;

    private String code;

    private String mail;

    private boolean send;

    public MailVerifyEntity() {
    }

    public MailVerifyEntity(UUID uuid, String code, String mail, boolean send) {
        this.uuid = uuid;
        this.code = code;
        this.mail = mail;
        this.send = send;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailVerifyEntity that)) return false;
        return send == that.send && Objects.equals(uuid, that.uuid) && Objects.equals(code, that.code) && Objects.equals(mail, that.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, code, mail, send);
    }
}
