package by.skachkovdmitry.audit.core.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PageOfReport {
    private int number;

    private int size;

    private int totalPages;

    private long totalElements;

    private boolean first;

    private int numberOfElements;

    private boolean last;

    private List<Report> content;
}
