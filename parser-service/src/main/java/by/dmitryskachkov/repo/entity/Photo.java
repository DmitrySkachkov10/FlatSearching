package by.dmitryskachkov.repo.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "flat", name = "photo")
public class Photo {

    @Id
    private UUID photo_uuid;

    @Column(name = "url_photo")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "uuid")
    private Flat flat;

    public Photo() {
    }

    public Photo(UUID photo_uuid, String photoUrl, Flat flat) {
        this.photo_uuid = photo_uuid;
        this.photoUrl = photoUrl;
        this.flat = flat;
    }

    public UUID getPhoto_uuid() {
        return photo_uuid;
    }

    public void setPhoto_uuid(UUID photo_uuid) {
        this.photo_uuid = photo_uuid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo photo)) return false;
        return Objects.equals(photo_uuid, photo.photo_uuid) && Objects.equals(photoUrl, photo.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photo_uuid, photoUrl);
    }
}
