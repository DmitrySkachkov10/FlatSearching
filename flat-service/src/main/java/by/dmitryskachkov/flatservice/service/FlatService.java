package by.dmitryskachkov.flatservice.service;

import by.dmitryskachkov.flatservice.core.EntityDtoMapper;
import by.dmitryskachkov.flatservice.core.dto.Flat;
import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.repo.api.IFlatRepo;
import by.dmitryskachkov.flatservice.repo.entity.FlatEntity;
import by.dmitryskachkov.flatservice.service.api.IFlatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlatService implements IFlatService {

    private IFlatRepo flatRepo;

    private final EntityManager entityManager;

    public FlatService(IFlatRepo flatRepo, EntityManager entityManager) {
        this.flatRepo = flatRepo;
        this.entityManager = entityManager;
    }

    @Override
    public PageOfFlat getPageOfFlat(FlatFilter flatFilter) {
        Specification<FlatEntity> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.between(root.get("price"), flatFilter.getPriceFrom(), flatFilter.getPriceTo()));
            predicates.add(cb.between(root.get("bedrooms"), flatFilter.getBedroomsFrom(), flatFilter.getBedroomsTo()));
            predicates.add(cb.between(root.get("area"), flatFilter.getAreaFrom(), flatFilter.getAreaTo()));

            if (flatFilter.isPhoto()) {
                predicates.add(cb.isNotNull(root.get("photos")));
            }
            if (!flatFilter.getFloors().isEmpty()) {
                predicates.add(root.get("floor").in(flatFilter.getFloors()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(flatFilter.getPage(), flatFilter.getSize());
        Page<FlatEntity> resultPage = flatRepo.findAll(specification, pageable);

        return new PageOfFlat(resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(),
                resultPage.getTotalElements(), resultPage.isFirst(), resultPage.getNumberOfElements(),
                resultPage.isLast(), EntityDtoMapper.INSTANCE.flatListToFlatDTOList(resultPage.getContent()));
    }
}
