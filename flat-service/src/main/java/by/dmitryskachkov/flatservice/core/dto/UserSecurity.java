package by.dmitryskachkov.flatservice.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class UserSecurity implements GrantedAuthority {

    private String uuid;
    private String mail;

    private String fio;
    private String role;

    public UserSecurity() {
    }

    public UserSecurity(String uuid, String mail, String fio, String role) {
        this.uuid = uuid;
        this.mail = mail;
        this.fio = fio;
        this.role = role;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return "ROLE_" + role;
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSecurity that)) return false;
        return Objects.equals(uuid, that.uuid) && Objects.equals(mail, that.mail) && Objects.equals(fio, that.fio) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, mail, fio, role);
    }
}
