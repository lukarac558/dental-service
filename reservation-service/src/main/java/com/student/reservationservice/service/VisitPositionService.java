package com.student.reservationservice.service;

import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitPosition;
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

    public List<VisitPosition> addVisitPositions(VisitEntity visitEntity, List<Long> serviceTypeIds) {
        return serviceTypeIds.stream()
                .map(typeId -> addVisitPosition(new VisitPosition(visitEntity, typeId)))
                .collect(Collectors.toList());
    }

    public VisitPosition addVisitPosition(VisitPosition visitPosition) {
        return visitPositionRepository.save(visitPosition);
    }

    public Optional<VisitPosition> findVisitPositionByVId(Long id) {
        return visitPositionRepository.findVisitPositionById(id);
    }

    public List<VisitPosition> findVisitPositionByVisitId(Long visitId) {
        return visitPositionRepository.findVisitPositionsByVisitId(visitId);
    }

    @Transactional
    public void deleteVisitPosition(Long id) {
        visitPositionRepository.deleteVisitPositionById(id);
    }
}
