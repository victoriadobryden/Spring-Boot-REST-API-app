package com.example.taskblock2.repositories;

import com.example.taskblock2.data.Case;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import jakarta.persistence.*;
import java.util.List;

@Repository
public class CustomCaseRepositoryImpl implements CustomCaseRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Case> findAllWithInvestigator(Specification<Case> spec, Pageable pageable) {

        TypedQuery<Case> typedQuery = fetchInvestigator(spec);
        long total = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Case> results = typedQuery.getResultList();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<Case> findAllWithInvestigator(Specification<Case> spec) {
        TypedQuery<Case> typedQuery = fetchInvestigator(spec);
        return typedQuery.getResultList();
    }

    private TypedQuery<Case> fetchInvestigator(Specification<Case> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Case> query = cb.createQuery(Case.class);
        Root<Case> root = query.from(Case.class);
        root.fetch("investigator", JoinType.LEFT);
        query.where(spec.toPredicate(root, query, cb));
        return entityManager.createQuery(query);
    }
}
