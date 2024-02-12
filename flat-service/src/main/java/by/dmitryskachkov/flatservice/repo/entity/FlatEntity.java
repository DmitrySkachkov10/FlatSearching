package by.dmitryskachkov.flatservice.repo.entity;

import by.dmitryskachkov.flatservice.core.enums.OfferType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(schema = "flat", name = "flat")
public class FlatEntity {

    @Id
    private UUID uuid;


    @Column(name = "dt_create")
    private LocalDateTime dtCreate;
    @Column(name = "dt_update")
    @Version
    private LocalDateTime dtUpdate;

    private OfferType offerType;

    private String description;

    private float area;

    private int price;

    private int bedrooms;

    private int floor;

    @Column(name = "original_url", unique = true)
    private String originalUrl;

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos;

    public FlatEntity() {
    }

    public FlatEntity(UUID uuid, LocalDateTime dtCreate, LocalDateTime dtUpdate, OfferType offerType, String description, float area, int price, int bedrooms, int floor, String originalUrl, Set<Photo> photos) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.offerType = offerType;
        this.description = description;
        this.area = area;
        this.price = price;
        this.bedrooms = bedrooms;
        this.floor = floor;
        this.originalUrl = originalUrl;
        this.photos = photos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }


    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatEntity that)) return false;
        return Float.compare(area, that.area) == 0 && price == that.price && bedrooms == that.bedrooms && floor == that.floor && Objects.equals(uuid, that.uuid) && Objects.equals(dtCreate, that.dtCreate) && Objects.equals(dtUpdate, that.dtUpdate) && offerType == that.offerType && Objects.equals(description, that.description) && Objects.equals(originalUrl, that.originalUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, dtCreate, dtUpdate, offerType, description, area, price, bedrooms, floor, originalUrl);
    }
}
