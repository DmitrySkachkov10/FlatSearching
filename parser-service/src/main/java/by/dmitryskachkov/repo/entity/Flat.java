package by.dmitryskachkov.repo.entity;


import by.dmitryskachkov.core.enums.OfferType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(schema = "flat", name = "flat")
public class Flat {

    @Column(name = "original_url")
    @Id
    private String originalUrl;


    private UUID uuid;
    @Column(name = "dt_create")
    private LocalDateTime createDate;
    @Column(name = "dt_update")
    @Version
    private LocalDateTime updateDate;
    @Enumerated(EnumType.STRING)
    private OfferType offerType;
    private String description; //todo sdelat`
    private int floor;
    private int bedrooms;
    private int price;
    private float area;
    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos;

    public Flat() {
    }

    public Flat(UUID uuid, LocalDateTime createDate, LocalDateTime updateDate, OfferType offerType, String description, int floor, int bedrooms, String originalUrl, int price, float area, Set<Photo> photos) {
        this.uuid = uuid;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.offerType = offerType;
        this.description = description;
        this.floor = floor;
        this.bedrooms = bedrooms;
        this.originalUrl = originalUrl;
        this.price = price;
        this.area = area;
        this.photos = photos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
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
        if (!(o instanceof Flat that)) return false;
        return floor == that.floor && bedrooms == that.bedrooms && Float.compare(area, that.area) == 0 && Objects.equals(uuid, that.uuid) && Objects.equals(createDate, that.createDate) && Objects.equals(updateDate, that.updateDate) && offerType == that.offerType && Objects.equals(description, that.description) && Objects.equals(originalUrl, that.originalUrl) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, createDate, updateDate, offerType, description, floor, bedrooms, originalUrl, price, area);
    }

    @Override
    public String toString() {
        return "FlatEntity{" +
                ", originalUrl='" + originalUrl;
    }
}

