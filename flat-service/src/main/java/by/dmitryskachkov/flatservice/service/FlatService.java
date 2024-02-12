package by.dmitryskachkov.flatservice.service;

import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.repo.api.IFlatRepo;
import by.dmitryskachkov.flatservice.repo.entity.FlatEntity;
import by.dmitryskachkov.flatservice.service.api.IFlatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FlatEntity> query = cb.createQuery(FlatEntity.class);
        Root<FlatEntity> root = query.from(FlatEntity.class);
        List<Predicate> predicateList = new ArrayList<>();


    }
}
