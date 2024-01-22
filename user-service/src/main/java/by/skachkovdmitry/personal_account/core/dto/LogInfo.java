package by.skachkovdmitry.personal_account.core.dto;

import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;

import java.util.Objects;

public class LogInfo {

    private UserSecurity user;

    private String essenceType;

    private String text;

    private String id;

    public LogInfo() {
    }

    public LogInfo(UserSecurity user, String essenceType, String text, String id) {
        this.user = user;
        this.essenceType = essenceType;
        this.text = text;
        this.id = id;
    }

    public UserSecurity getUser() {
        return user;
    }

    public void setUser(UserSecurity user) {
        this.user = user;
    }

    public String getEssenceType() {
        return essenceType;
    }

    public void setEssenceType(String essenceType) {
        this.essenceType = essenceType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        if (!(o instanceof LogInfo logInfo)) return false;
        return Objects.equals(user, logInfo.user) && Objects.equals(essenceType, logInfo.essenceType) && Objects.equals(text, logInfo.text) && Objects.equals(id, logInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, essenceType, text, id);
    }
}
