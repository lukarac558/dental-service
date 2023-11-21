package com.student.reservationservice.service;

import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.exception.IncorrectValueException;
import com.student.api.exception.NotFoundException;
import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitLockEntity;
import com.student.reservationservice.entity.VisitPositionEntity;
import com.student.reservationservice.repository.VisitLockRepository;
import com.student.reservationservice.repository.VisitPositionRepository;
import com.student.reservationservice.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.student.api.exception.handler.ErrorConstants.*;

@Service
@RequiredArgsConstructor
public class VisitPositionService {
    private final VisitPositionRepository visitPositionRepository;
    private final VisitRepository visitRepository;
    private final VisitLockRepository visitLockRepository;


    public List<VisitPositionEntity> createVisitPositions(VisitEntity visitEntity, List<ServiceTypeDto> serviceTypes, Long lockId) {
        validateIfStartDateIsAvailable(visitEntity.getStartDate(), serviceTypes, lockId);
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

    private void validateIfStartDateIsAvailable(Timestamp startDate, List<ServiceTypeDto> serviceTypes, Long lockId) {
        Long doctorId = getDoctorIdOrThrow(serviceTypes);
        List<VisitLockEntity> locks = new ArrayList<>(getLockToDoctorInSameTime(doctorId, startDate));
        locks.sort(Comparator.comparing(VisitLockEntity::getId));
        VisitLockEntity firstEntity = locks.get(0);
        VisitLockEntity lastEntity = locks.get(locks.size() - 1);

        if (locks.size() > 1 && lastEntity.getId().equals(lockId)) {
            visitLockRepository.deleteVisitLockEntitiesByDoctorIdAndStartDate(doctorId, startDate);
            throw new IncorrectValueException(String.format(INCORRECT_START_DATE_MESSAGE, startDate));
        } else if (locks.size() > 1 && !firstEntity.getId().equals(lockId)) {
            throw new IncorrectValueException(String.format(INCORRECT_START_DATE_MESSAGE, startDate));
        }
    }

    private List<VisitLockEntity> getLockToDoctorInSameTime(Long doctorId, Timestamp startDate) {
        return visitLockRepository.findVisitLockEntitiesByDoctorIdAndStartDate(doctorId, startDate);
    }

    private Long getDoctorIdOrThrow(List<ServiceTypeDto> serviceTypes) {
        List<Long> serviceTypeIds = serviceTypes.stream().map(ServiceTypeDto::getId).toList();
        return serviceTypes.stream().map(ServiceTypeDto::getDoctorId).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(DOCTOR_NOT_FOUND_MESSAGE, serviceTypeIds)));
    }
}
