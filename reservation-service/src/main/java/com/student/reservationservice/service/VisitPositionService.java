package com.student.reservationservice.service;

import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.exception.NotFoundException;
import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitPositionEntity;
import com.student.reservationservice.repository.VisitPositionRepository;
import com.student.reservationservice.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.student.api.exception.handler.ErrorConstants.VISIT_POSITION_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class VisitPositionService {
    private final VisitPositionRepository visitPositionRepository;
    private final VisitRepository visitRepository;


    public List<VisitPositionEntity> createVisitPositions(VisitEntity visitEntity, List<ServiceTypeDto> serviceTypes) {
        return serviceTypes.stream()
                .map(type -> createVisitPosition(new VisitPositionEntity(visitEntity, type.getId())))
                .collect(Collectors.toList());
    }

    public VisitPositionEntity createVisitPosition(VisitPositionEntity visitPositionEntity) {
        return visitPositionRepository.save(visitPositionEntity);
    }

    public Optional<VisitPositionEntity> findVisitPositionByVId(Long id) {
        return visitPositionRepository.findVisitPositionById(id);
    }

    public List<VisitPositionEntity> findVisitPositionByVisitId(Long visitId) {
        return visitPositionRepository.findVisitPositionsByVisitId(visitId);
    }

    public VisitEntity getVisitEntityOrThrow(Long visitId) {
        return visitRepository.findVisitById(visitId)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_POSITION_NOT_FOUND_MESSAGE, visitId)));
    }

    @Transactional
    public void deleteVisitPosition(Long id) {
        visitPositionRepository.deleteVisitPositionById(id);
    }
}
