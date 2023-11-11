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

import static com.student.reservationservice.service.visit.VisitService.VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS;


public class VisitSpecification implements Specification<VisitEntity> {
    private final Long userId;
    private final boolean approved;

    public VisitSpecification(Long userId, boolean approved) {
        this.userId = userId;
        this.approved = approved;
    }

    @Override
    public Predicate toPredicate(Root<VisitEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(
                root.get("patientId"),
                userId
        ));

        predicates.add(criteriaBuilder.equal(
                root.get("approved"),
                approved
        ));

        if (!approved) {
            predicates.add(criteriaBuilder.greaterThan(
                    root.get("reservationDate"),
                    geTimestampApprovalCondition()
            ));
        }
        return criteriaBuilder.and(
                predicates.toArray(new Predicate[0])
        );
    }

    private Timestamp geTimestampApprovalCondition() {
        long timeInMs = System.currentTimeMillis() - VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS;
        return new Timestamp(timeInMs);
    }
}
