package com.student.reservationservice.visit.visit.service;

import com.student.reservationservice.visit.visit.entity.Visit;
import com.student.reservationservice.visit.visit.exception.VisitCancellationNotPossibleException;
import com.student.reservationservice.visit.visit.exception.VisitNotFoundException;
import com.student.reservationservice.visit.visit.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {
    private static final int VISIT_CANCELLATION_LIMIT_IN_HOURS = 24;
    private final VisitRepository visitRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit addOrUpdateVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    public Optional<Visit> findVisitById(Long id) {
        return visitRepository.findVisitById(id);
    }

    public List<Visit> findVisitsByUserId(Long userId) {
        return visitRepository.findVisitsByUserId(userId);
    }

    @Transactional
    public void deleteVisit(Long id) {
        Visit visit = visitRepository.findVisitById(id)
                .orElseThrow(() -> new VisitNotFoundException(id));

        Timestamp currentDate = Timestamp.from(Instant.now());
        Timestamp visitStartDate = visit.getStartDate();

        Duration remainingTimeToVisit = Duration.between(currentDate.toInstant(), visitStartDate.toInstant());

        if (remainingTimeToVisit.toHours() >= VISIT_CANCELLATION_LIMIT_IN_HOURS) {
            visitRepository.deleteVisitById(id);
        } else {
            throw new VisitCancellationNotPossibleException(id, VISIT_CANCELLATION_LIMIT_IN_HOURS, remainingTimeToVisit.toHours());
        }
    }
}
