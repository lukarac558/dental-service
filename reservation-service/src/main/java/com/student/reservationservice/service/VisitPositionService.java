package com.student.reservationservice.service;

import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitPositionEntity;
import com.student.reservationservice.repository.VisitPositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitPositionService {
    private final VisitPositionRepository visitPositionRepository;

    @Autowired
    public VisitPositionService(VisitPositionRepository visitPositionRepository) {
        this.visitPositionRepository = visitPositionRepository;
    }

    public List<VisitPositionEntity> createVisitPositions(VisitEntity visitEntity, List<Long> serviceTypeIds) {
        return serviceTypeIds.stream()
                .map(typeId -> createVisitPosition(new VisitPositionEntity(visitEntity, typeId)))
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

    @Transactional
    public void deleteVisitPosition(Long id) {
        visitPositionRepository.deleteVisitPositionById(id);
    }
}
