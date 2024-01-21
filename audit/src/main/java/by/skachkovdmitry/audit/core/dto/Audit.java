package by.skachkovdmitry.audit.core.dto;

import by.skachkovdmitry.audit.core.EssenceType;

import java.util.Objects;
import java.util.UUID;

public class Audit {

    private String uuid;

    private long dt_create;

    private User user;

    private String text;

    private EssenceType essenceType;

    private String id;

    public Audit() {
    }

    public Audit(String uuid, long dt_create, User user, String text, EssenceType essenceType, String id) {
        this.uuid = uuid;
        this.dt_create = dt_create;
        this.user = user;
        this.text = text;
        this.essenceType = essenceType;
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getDt_create() {
        return dt_create;
    }

    public void setDt_create(long dt_create) {
        this.dt_create = dt_create;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
        if (!(o instanceof Audit audit)) return false;
        return dt_create == audit.dt_create && Objects.equals(uuid, audit.uuid) && Objects.equals(user, audit.user) && Objects.equals(text, audit.text) && essenceType == audit.essenceType && Objects.equals(id, audit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, dt_create, user, text, essenceType, id);
    }
}
