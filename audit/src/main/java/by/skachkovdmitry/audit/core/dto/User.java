package by.skachkovdmitry.audit.core.dto;

import java.util.Objects;

public class User {
    private String uuid;

    private String mail;

    private String fio;

    private String role;

    public User() {
    }


    public User(String uuid, String mail, String fio, String role) {
        this.uuid = uuid;
        this.mail = mail;
        this.fio = fio;
        this.role = role;
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
        if (!(o instanceof User user)) return false;
        return Objects.equals(uuid, user.uuid) && Objects.equals(mail, user.mail) && Objects.equals(fio, user.fio) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, mail, fio, role);
    }
}
