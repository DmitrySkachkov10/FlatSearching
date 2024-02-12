package by.dmitryskachkov.flatservice.service.api;

import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;

public interface IFlatService {
    PageOfFlat getPageOfFlat(FlatFilter flatFilter);
}
