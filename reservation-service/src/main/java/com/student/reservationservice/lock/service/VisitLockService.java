package com.student.reservationservice.lock.service;

import com.student.reservationservice.lock.entity.VisitLock;
import com.student.reservationservice.lock.exception.VisitLockExistsException;
import com.student.reservationservice.lock.repository.VisitLockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VisitLockService {
    private final VisitLockRepository visitLockRepository;

    @Autowired
    public VisitLockService(VisitLockRepository visitLockRepository) {
        this.visitLockRepository = visitLockRepository;
    }


    public VisitLock lock(VisitLock visitLock) {
        if (ifExistsVisitLockWithGivenUserIdAndStartDate(visitLock.getUserId(), visitLock.getStartDate())) {
            throw new VisitLockExistsException();
        }
        return visitLockRepository.save(visitLock);
    }

    public boolean ifExistsVisitLockWithGivenUserIdAndStartDate(Long doctorId, Timestamp start) {
        Timestamp before = getTwoHoursBefore(start);
        return visitLockRepository.findVisitLocksByUserIdAndStartDateBetween(doctorId, before, start).stream().findAny().isPresent();
    }

    @Transactional
    public void unlock(Long id) {
        visitLockRepository.deleteVisitLockById(id);
    }

    private Timestamp getTwoHoursBefore(Timestamp startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate.getTime());
        cal.add(Calendar.HOUR, -2);
        return new Timestamp(cal.getTime().getTime());
    }


}
