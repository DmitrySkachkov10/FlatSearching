package by.dmitryskachkov.flatservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageOfFlat {
    private int number;
    private int size;
    private int total_pages;
    private long total_elements;

    private boolean first;

    private int number_of_elements;

    private boolean last;

    private List<FlatDTO> content;

}
