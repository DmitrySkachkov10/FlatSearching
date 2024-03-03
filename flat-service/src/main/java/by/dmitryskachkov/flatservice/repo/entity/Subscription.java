package by.dmitryskachkov.flatservice.repo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "subscription", schema = "flat")
public class Subscription {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "user_uuid")
    private UUID userUuid;

    @Column(name = "dt_create", columnDefinition = "timestamp without time zone")
    private LocalDateTime dtCreate;

    @Column(name = "dt_update", columnDefinition = "timestamp without time zone")
    @Version
    private LocalDateTime dtUpdate;

    @Column(name = "price_from")
    private int priceFrom;

    @Column(name = "price_to")
    private int priceTo;

    @Column(name = "bedrooms_from")
    private int bedroomsFrom;

    @Column(name = "bedrooms_to")
    private int bedroomsTo;

    @Column(name = "area_from")
    private float areaFrom;

    @Column(name = "area_to")
    private float areaTo;

    @Column(name = "floors")
    private List<Integer> floors;

    @Column(name = "photo")
    private Boolean photo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription that)) return false;
        return priceFrom == that.priceFrom && priceTo == that.priceTo && bedroomsFrom == that.bedroomsFrom && bedroomsTo == that.bedroomsTo && Float.compare(areaFrom, that.areaFrom) == 0 && Float.compare(areaTo, that.areaTo) == 0 && Objects.equals(uuid, that.uuid) && Objects.equals(userUuid, that.userUuid) && Objects.equals(dtCreate, that.dtCreate) && Objects.equals(dtUpdate, that.dtUpdate) && Objects.equals(floors, that.floors) && Objects.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, userUuid, dtCreate, dtUpdate, priceFrom, priceTo, bedroomsFrom, bedroomsTo, areaFrom, areaTo, floors, photo);
    }

    @Override
    public String
    toString() {
        return "Subscription{" +
                "uuid=" + uuid +
                ", userUuid=" + userUuid +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                ", bedroomsFrom=" + bedroomsFrom +
                ", bedroomsTo=" + bedroomsTo +
                ", areaFrom=" + areaFrom +
                ", areaTo=" + areaTo +
                ", floors=" + floors +
                ", photo=" + photo +
                '}';
    }
}