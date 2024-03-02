package by.dmitryskachkov.flatservice.repo.entity;


import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "flat", name = "bookmarks")
public class Bookmark {

    @Id
    @Column(name = "user_uuid")
    private UUID userUuid;


    @Column(name = "flat_uuid")
    private UUID flatUuid;


    public Bookmark() {
    }

    public Bookmark(UUID userUuid, UUID flatUuid) {
        this.userUuid = userUuid;
        this.flatUuid = flatUuid;
    }

    public UUID getUserUuid() {

        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public UUID getFlatUuid() {
        return flatUuid;
    }

    public void setFlatUuid(UUID flatUuid) {
        this.flatUuid = flatUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bookmark bookmark)) return false;
        return Objects.equals(userUuid, bookmark.userUuid) && Objects.equals(flatUuid, bookmark.flatUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, flatUuid);
    }
}

