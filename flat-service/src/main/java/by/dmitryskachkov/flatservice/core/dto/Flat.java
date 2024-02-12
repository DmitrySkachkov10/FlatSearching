package by.dmitryskachkov.flatservice.core.dto;

import by.dmitryskachkov.flatservice.config.properies.LocalDateTimeUnixTimestampSerializer;
import by.dmitryskachkov.flatservice.config.properies.UnixTimestampToLocalDateTimeDeserializer;
import by.dmitryskachkov.flatservice.core.enums.OfferType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flat {

    private UUID uuid;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime dtCreate;

    @JsonSerialize(using = LocalDateTimeUnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime dtUpdate;

    private OfferType offerType;

    private String description;

    private float area;

    private int price;

    private int bedrooms;

    private int floor;

    private List<String> photoUrls;

    private String originalUrl;
}
