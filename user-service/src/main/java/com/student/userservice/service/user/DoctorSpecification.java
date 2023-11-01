package com.student.userservice.service.user;

import com.student.api.dto.common.Role;
import com.student.api.dto.user.DoctorSearchRequestDto;
import com.student.userservice.entity.UserEntity;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DoctorSpecification implements Specification<UserEntity> {

    private final DoctorSearchRequestDto searchRequest;

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(
                cb.equal(
                        root.join("roles").get("role"),
                        Role.DOCTOR
                )
        );

        if(!searchRequest.getName().isBlank()) {
            String name = searchRequest.getName().toLowerCase();
            predicates.add(
                    cb.or(
                            cb.like(cb.concat(cb.concat(cb.lower(root.get("name")), " "), cb.lower(root.get("surname"))), "%"+ name +"%"),
                            cb.like(cb.concat(cb.concat(cb.lower(root.get("surname")), " "), cb.lower(root.get("name"))), "%"+ name +"%")
                    )
            );
        }

        if(!searchRequest.getService().isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(
                                    root.join("serviceTypes").get("name")
                            ),
                            "%"+searchRequest.getService().toLowerCase()+"%"
                    )
            );
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
