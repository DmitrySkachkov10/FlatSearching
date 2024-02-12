package by.dmitryskachkov.flatservice.core.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FlatFilter {

    private int page;

    private int size;

    private int priceFrom; //yes

    private int priceTo; //yes

    private int bedroomsFrom; //yes

    private int bedroomsTo; //yes

    private float areaFrom; //yes

    private float areaTo;//yes

    private List<Integer> floors; //yes

    private boolean photo; //yes

}
