package by.skachkovdmitry.audit.core.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class UserSecurity implements GrantedAuthority {

    private String mail;
    private String role;

    public UserSecurity() {
    }

    public UserSecurity(String mail, String role) {
        this.mail = mail;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSecurity that)) return false;
        return Objects.equals(mail, that.mail) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail, role);
    }

}
