package by.dmitryskachkov.flatservice.core.dto;


import by.dmitryskachkov.flatservice.config.properies.LocalDateTimeUnixTimestampSerializer;
import by.dmitryskachkov.flatservice.config.properies.UnixTimestampToLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    private UUID uuid;

    private UUID userUuid;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime dtCreate;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime dtUpdate;

    private int priceFrom;

    private int priceTo;

    private int bedroomsFrom;

    private int bedroomsTo;

    private float areaFrom;

    private float areaTo;

    private List<Integer> floors;

    private boolean photo;

}