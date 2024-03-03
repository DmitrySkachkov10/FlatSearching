package by.dmitryskachkov.flatservice.core.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatFilter {

    private int page;

    private int size;

    private int priceFrom;

    private int priceTo;

    private int bedroomsFrom;

    private int bedroomsTo;

    private float areaFrom;

    private float areaTo;

    private List<Integer> floors;

    private boolean photo;
}
