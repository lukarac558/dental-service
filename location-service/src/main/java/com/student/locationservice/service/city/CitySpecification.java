package com.student.locationservice.service.city;

import com.student.locationservice.entity.CityEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CitySpecification implements Specification<CityEntity> {

    private final String name;
    private final Set<Long> voivodeshipIds;

    public CitySpecification(Set<Long> voivodeshipIds, String name) {
        this.name = name.toLowerCase();
        this.voivodeshipIds = voivodeshipIds;
    }

    @Override
    public Predicate toPredicate(Root<CityEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(!name.isBlank()) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%"+name+"%"
            ));
        }
        if(!voivodeshipIds.isEmpty()) {
            predicates.add(root.join("voivodeship").get("id").in(voivodeshipIds));
        }
        return criteriaBuilder.and(
                predicates.toArray(new Predicate[0])
        );

    }
}
