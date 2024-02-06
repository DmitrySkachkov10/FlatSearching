package by.dmitryskachkov.repo.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "flat", name = "photos")
public class Photos {

    @Id
    @Column(name = "url_photo")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "uuid")
    private FlatEntity flat;

    public Photos() {
    }

    public Photos(String photoUrl, FlatEntity flat) {
        this.photoUrl = photoUrl;
        this.flat = flat;
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
        if (!(o instanceof Photos photos)) return false;
        return Objects.equals(photoUrl, photos.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoUrl);
    }
}
