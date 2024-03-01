package by.dmitryskachkov.flatservice.core.filter;


import by.dmitryskachkov.flatservice.config.properies.LocalDateTimeUnixTimestampSerializer;
import by.dmitryskachkov.flatservice.config.properies.UnixTimestampToLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


public class User   {
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
    public String toString() {
        return "User{" +
                "status='" + status + '\'' +
                '}';
    }
}