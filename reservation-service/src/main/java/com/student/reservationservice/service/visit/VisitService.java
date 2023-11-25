package com.student.reservationservice.service.visit;

import com.student.api.dto.reservation.StartEndTime;
import com.student.api.dto.reservation.VisitSearchRequestDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.exception.ApprovalForbiddenException;
import com.student.api.exception.CancellationForbiddenException;
import com.student.api.exception.IncorrectValueException;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimeHelper;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.client.UserClient;
import com.student.reservationservice.entity.CalendarDayEntity;
import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitLockEntity;
import com.student.reservationservice.entity.VisitPositionEntity;
import com.student.reservationservice.repository.CalendarDayRepository;
import com.student.reservationservice.repository.VisitLockRepository;
import com.student.reservationservice.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.student.api.exception.handler.ErrorConstants.*;
import static com.student.api.util.TimeHelper.ONE_MINUTE_IN_MS;

@Service
@AllArgsConstructor
public class VisitService {
    private static final int VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MINUTES = 15;
    public static final int VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS = VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MINUTES * ONE_MINUTE_IN_MS;
    private static final int VISIT_CANCELLATION_LIMIT_IN_HOURS = 24;
    private final VisitRepository visitRepository;
    private final UserClient userClient;
    private final CalendarDayRepository calendarDayRepository;
    private final VisitLockRepository visitLockRepository;

    public VisitEntity addVisitOrThrow(VisitEntity visitEntity, List<ServiceTypeDto> serviceTypes, Long lockId) {
        validateIfStartDateIsAvailable(visitEntity.getStartDate(), serviceTypes, lockId);
        return visitRepository.save(visitEntity);
    }

    public VisitEntity updateVisit(VisitEntity visitEntity) {
        return visitRepository.save(visitEntity);
    }

    @Transactional
    public void deleteVisit(Long id) {
        VisitEntity visitEntity = visitRepository.findVisitById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));

        Timestamp currentDate = Timestamp.from(Instant.now());
        Timestamp visitStartDate = visitEntity.getStartDate();
        Duration remainingTimeToVisit = Duration.between(currentDate.toInstant(), visitStartDate.toInstant());

        if (remainingTimeToVisit.toHours() >= VISIT_CANCELLATION_LIMIT_IN_HOURS) {
            visitRepository.deleteVisitById(id);
        } else {
            throw new CancellationForbiddenException(String.format(VISIT_CANCELLATION_FORBIDDEN_MESSAGE, id, VISIT_CANCELLATION_LIMIT_IN_HOURS, remainingTimeToVisit.toHours()));
        }
    }

    @Transactional
    public void deleteNotApprovedVisitsWithExpiredTime() {
        visitRepository.findByApproved(false).stream()
                .filter(v -> !isApprovalPossible(v.getReservationDate()))
                .forEach(v -> visitRepository.deleteVisitById(v.getId()));
    }

    public Optional<VisitEntity> findVisitById(Long id) {
        return visitRepository.findVisitById(id);
    }

    public Page<VisitEntity> findApprovedVisitsByRequest(VisitSearchRequestDto searchRequestDto) {
        return findVisitsByRequest(searchRequestDto, true);
    }

    public Page<VisitEntity> findNotApprovedVisitsByRequest(VisitSearchRequestDto searchRequestDto) {
        return findVisitsByRequest(searchRequestDto, false);
    }

    public List<String> findAvailableVisitTimesInIntervals(List<ServiceTypeDto> serviceTypes) {
        Long doctorId = getDoctorIdOrThrow(serviceTypes);
        List<CalendarDayEntity> calendarDayEntities = calendarDayRepository.findCalendarDaysByDoctorId(doctorId);

        List<Long> allDoctorServiceTypeIds = userClient.getServiceTypesByDoctorId(doctorId).stream().map(ServiceTypeDto::getId).toList();
        Map<CalendarDayEntity, List<VisitEntity>> visitsToCalendarDay = fillVisitsToDay(calendarDayEntities, allDoctorServiceTypeIds);
        Map<CalendarDayEntity, List<StartEndTime>> visitsTimeToCalendarDay = fillVisitTimesToDay(visitsToCalendarDay);
        Time visitDuration = calculateVisitDuration(serviceTypes);
        List<StartEndTime> possibleVisitTimes =
                getAvailableVisitTimes(visitsTimeToCalendarDay).stream()
                        .filter(visitTime -> TimeHelper.isEnough(visitTime, visitDuration))
                        .toList();

        return TimeHelper.getStartDatesInIntervals(possibleVisitTimes, visitDuration, VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS);
    }

    public void setVisitAsApprovedOrThrow(VisitEntity visitEntity) {
        if (visitEntity.isApproved()) {
            throw new ApprovalForbiddenException(String.format(VISIT_IS_ALREADY_APPROVED_MESSAGE, visitEntity.getId()));
        } else if (isApprovalPossible(visitEntity.getReservationDate())) {
            visitEntity.setApproved(true);
        } else {
            throw new ApprovalForbiddenException(String.format(VISIT_APPROVAL_FORBIDDEN_MESSAGE, visitEntity.getId(), VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MINUTES));
        }
    }

    public void validateStartDateOrThrow(List<ServiceTypeDto> serviceTypes, String startDate) {
        TimestampFormatParser.parseOrThrow(startDate);
        boolean isStartDateCorrect =
                findAvailableVisitTimesInIntervals(serviceTypes).stream()
                        .anyMatch(time -> time.equals(startDate));

        if (!isStartDateCorrect) {
            throw new IncorrectValueException(String.format(INCORRECT_START_DATE_MESSAGE, startDate));
        }
    }

    public Long getDoctorIdOrThrow(List<ServiceTypeDto> serviceTypes) {
        List<Long> serviceTypeIds = serviceTypes.stream().map(ServiceTypeDto::getId).toList();
        return serviceTypes.stream().map(ServiceTypeDto::getDoctorId).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(DOCTOR_NOT_FOUND_MESSAGE, serviceTypeIds)));
    }

    public Time calculateVisitDuration(List<ServiceTypeDto> serviceTypes) {
        return serviceTypes.stream()
                .map(time -> Time.valueOf(time.getDurationTime()))
                .reduce(TimeHelper::getSummed)
                .orElse(Time.valueOf("00:00:00"));
    }

    private boolean isApprovalPossible(Timestamp reservationDate) {
        long currentTimeMs = System.currentTimeMillis();
        long reservationTimeMs = reservationDate.getTime();
        return (currentTimeMs - reservationTimeMs) < VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS;
    }

    private Page<VisitEntity> findVisitsByRequest(VisitSearchRequestDto searchRequestDto, boolean approved) {
        return visitRepository.findAll(
                new VisitSpecification(searchRequestDto.getUserId(), approved),
                searchRequestDto.pageable()
        );
    }

    private Map<CalendarDayEntity, List<VisitEntity>> fillVisitsToDay(List<CalendarDayEntity> calendarDayEntities, List<Long> serviceTypeIds) {
        return calendarDayEntities.stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> visitRepository.findByStartDateIgnoringTimeAndServiceTypeIds(c.getStartDate(), serviceTypeIds).stream()
                                .filter(this::isVisitActual)
                                .toList()
                ));
    }

    private boolean isVisitActual(VisitEntity visitEntity) {
        if (visitEntity.isApproved()) {
            return true;
        } else {
            return isApprovalPossible(visitEntity.getReservationDate());
        }
    }

    private Map<CalendarDayEntity, List<StartEndTime>> fillVisitTimesToDay(Map<CalendarDayEntity, List<VisitEntity>> visitsToCalendarDay) {
        return visitsToCalendarDay.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(v -> {
                                    Timestamp start = v.getStartDate();
                                    List<Long> typeIds = v.getVisitPositions().stream()
                                            .map(VisitPositionEntity::getServiceTypeId)
                                            .toList();
                                    List<ServiceTypeDto> types = userClient.getServiceTypes(typeIds);
                                    Time totalTime = calculateVisitDuration(types);
                                    Timestamp end = TimeHelper.getEndDate(start, totalTime);
                                    return new StartEndTime(start, end);
                                })
                                .toList()
                ));
    }

    private List<StartEndTime> getAvailableVisitTimes(Map<CalendarDayEntity, List<StartEndTime>> visitsTimeToCalendarDay) {
        List<StartEndTime> availableVisitTimes = new ArrayList<>();

        visitsTimeToCalendarDay.forEach((day, visitTimes) -> {
            Timestamp workStart = day.getStartDate();
            Timestamp workEnd = TimeHelper.getEndDate(workStart, day.getWorkDuration());

            if (visitTimes.isEmpty()) {
                availableVisitTimes.add(new StartEndTime(workStart, workEnd));
            } else {
                List<StartEndTime> modifiableVisitTimes = new ArrayList<>(visitTimes);
                modifiableVisitTimes.sort(Comparator.comparing(StartEndTime::getStartTime));
                for (int i = 0; i < modifiableVisitTimes.size(); i++) {
                    StartEndTime current = modifiableVisitTimes.get(i);

                    if (i == 0 && workStart.before(current.getStartTime())) {
                        availableVisitTimes.add(new StartEndTime(workStart, current.getStartTime()));
                    }

                    if (i == modifiableVisitTimes.size() - 1) {
                        if (workEnd.after(current.getEndTime())) {
                            availableVisitTimes.add(new StartEndTime(current.getEndTime(), workEnd));
                        }
                        break;
                    } else {
                        StartEndTime next = modifiableVisitTimes.get(i + 1);
                        availableVisitTimes.add(new StartEndTime(current.getEndTime(), next.getStartTime()));
                    }
                }
            }
        });

        return availableVisitTimes;
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
        } else if (locks.size() == 1) {
            visitLockRepository.deleteVisitLockEntityById(lockId);
        }
    }

    private List<VisitLockEntity> getLockToDoctorInSameTime(Long doctorId, Timestamp startDate) {
        return visitLockRepository.findVisitLockEntitiesByDoctorIdAndStartDate(doctorId, startDate);
    }
}
