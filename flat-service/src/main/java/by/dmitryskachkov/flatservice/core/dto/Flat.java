package by.dmitryskachkov.flatservice.core.dto;

import by.dmitryskachkov.flatservice.core.enums.OfferType;
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

    private LocalDateTime dtCreate;

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
