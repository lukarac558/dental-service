package com.student.userservice.service.type;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.common.Role;
import com.student.api.dto.user.ServiceTypeSearchRequestDto;
import com.student.userservice.entity.ServiceTypeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TypeSepcification implements Specification<ServiceTypeEntity> {

    private final Info info;
    private final ServiceTypeSearchRequestDto searchRequestDto;

    @Override
    public Predicate toPredicate(Root<ServiceTypeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(searchRequestDto.getShowOnlyYour() && info.getRoles().contains(Role.DOCTOR)) {
            predicates.add(criteriaBuilder.equal(root.join("doctor").get("email"), info.getEmail()));
        }

        if(!searchRequestDto.getName().isBlank()) {
            predicates.add(
              criteriaBuilder.like(
                      criteriaBuilder.lower(root.get("name")),
                      "%"+searchRequestDto.getName().toLowerCase()+"%"
              )
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
