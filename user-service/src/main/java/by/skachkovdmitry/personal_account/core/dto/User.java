package by.skachkovdmitry.personal_account.core.dto;

import by.skachkovdmitry.personal_account.config.properties.LocalDateTimeUnixTimestampSerializer;
import by.skachkovdmitry.personal_account.config.properties.UnixTimestampToLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


public class User implements GrantedAuthority {
    private String uuid;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime dtCreate;
    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime dtUpdate;
    private String fio;
    private String mail;
    private String role;
    private String status;

    public User() {
    }

    public User(String uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate, String fio, String mail, String role, String status) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.fio = fio;
        this.mail = mail;
        this.role = role;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return dtCreate == user.dtCreate && dtUpdate == user.dtUpdate && Objects.equals(uuid, user.uuid) && Objects.equals(fio, user.fio) && Objects.equals(mail, user.mail) && Objects.equals(role, user.role) && Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, dtCreate, dtUpdate, fio, mail, role, status);
    }


    @Override
    @JsonIgnore
    public String getAuthority() {
        return "ROLE_" + role;
    }

    @JsonIgnore //чтобы не было круговой завиимости у jackson
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
    }


}