package com.student.reservationservice.visit.visitposition.service;

import com.student.reservationservice.visit.visitposition.entity.VisitPosition;
import com.student.reservationservice.visit.visitposition.repository.VisitPositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitPositionService {
    private final VisitPositionRepository visitPositionRepository;

    @Autowired
    public VisitPositionService(VisitPositionRepository visitPositionRepository) {
        this.visitPositionRepository = visitPositionRepository;
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
