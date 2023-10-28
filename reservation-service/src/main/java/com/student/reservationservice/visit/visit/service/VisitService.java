package com.student.reservationservice.visit.visit.service;

import com.student.reservationservice.common.exception.entity.CancellationForbiddenException;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.visit.visit.entity.Visit;
import com.student.reservationservice.visit.visit.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.VISIT_CANCELLATION_FORBIDDEN_MESSAGE;
import static com.student.reservationservice.common.exception.entity.ErrorConstants.VISIT_NOT_FOUND_MESSAGE;

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
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));

        Timestamp currentDate = Timestamp.from(Instant.now());
        Timestamp visitStartDate = visit.getStartDate();

        Duration remainingTimeToVisit = Duration.between(currentDate.toInstant(), visitStartDate.toInstant());

        if (remainingTimeToVisit.toHours() >= VISIT_CANCELLATION_LIMIT_IN_HOURS) {
            visitRepository.deleteVisitById(id);
        } else {
            throw new CancellationForbiddenException(String.format(VISIT_CANCELLATION_FORBIDDEN_MESSAGE, id, VISIT_CANCELLATION_LIMIT_IN_HOURS, remainingTimeToVisit.toHours()));
        }
    }
}
