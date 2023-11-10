package com.student.reservationservice.service.visit;

import com.student.reservationservice.entity.VisitEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class VisitSpecification implements Specification<VisitEntity> {
    private final Long userId;
    private final boolean visitUpcoming;

    public VisitSpecification(Long userId, boolean visitUpcoming) {
        this.userId = userId;
        this.visitUpcoming = visitUpcoming;
    }

    @Override
    public Predicate toPredicate(Root<VisitEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (userId > 0) {
            predicates.add(criteriaBuilder.equal(
                    root.get("patient_id"),
                    userId
            ));
        }

        CriteriaBuilder.In<Timestamp> currentTimestamp = criteriaBuilder.in(criteriaBuilder.currentTimestamp());

        if(visitUpcoming) {
            // data początku przed obecną datą
            predicates.add(criteriaBuilder.lessThan(
                    root.get("start_date"),
                    currentTimestamp
            ));
        } else {
            predicates.add(criteriaBuilder.greaterThan(
                    root.get("start_date"),
                    currentTimestamp
            ));
        }

        return criteriaBuilder.and(
                predicates.toArray(new Predicate[0])
        );
    }
}
