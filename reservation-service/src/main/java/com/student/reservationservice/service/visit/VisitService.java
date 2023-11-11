package com.student.reservationservice.service.visit;

import com.student.api.dto.reservation.VisitSearchRequestDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.exception.ApprovalForbiddenException;
import com.student.api.exception.CancellationForbiddenException;
import com.student.api.exception.IncorrectValueException;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.entity.CalendarDayEntity;
import com.student.reservationservice.entity.StartEndTime;
import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitPositionEntity;
import com.student.reservationservice.repository.CalendarDayRepository;
import com.student.reservationservice.repository.VisitRepository;
import com.student.reservationservice.user.UserClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.student.api.exception.handler.ErrorConstants.*;

@Service
@AllArgsConstructor
public class VisitService {
    private static final int VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MINUTES = 15;
    private static final int ONE_MINUTE_IN_MS = 60000;
    private static final int VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS = VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MINUTES * ONE_MINUTE_IN_MS;
    private static final int ONE_HOUR_IN_MS = 60 * ONE_MINUTE_IN_MS;
    private static final int VISIT_CANCELLATION_LIMIT_IN_HOURS = 24;
    private final VisitRepository visitRepository;
    private final UserClient userClient;
    private final CalendarDayRepository calendarDayRepository;

    public VisitEntity addOrUpdateVisit(VisitEntity visitEntity) {
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

    public Page<VisitEntity> findApprovedUpcomingVisitsByRequest(VisitSearchRequestDto searchRequestDto) {
        Page<VisitEntity> upcomingVisits = findUpcomingVisitsByRequest(searchRequestDto);
        List<VisitEntity> filteredVisits = upcomingVisits.getContent().stream()
                .filter(VisitEntity::isApproved)
                .toList();
        return new PageImpl<>(filteredVisits, upcomingVisits.getPageable(), filteredVisits.size());
    }

    public Page<VisitEntity> findHistoricalVisitsByPatientId(VisitSearchRequestDto searchRequestDto) {
        return visitRepository.findAll(
                new VisitSpecification(searchRequestDto.getUserId(), false),
                searchRequestDto.pageable()
        );
    }

    public List<VisitEntity> findNotApprovedVisitsByPatientId(Long patientId) {
        List<VisitEntity> notApprovedVisits = visitRepository.findVisitsByPatientIdAndApproved(patientId, false);
        return notApprovedVisits.stream().filter(v -> isApprovalPossible(v.getReservationDate())).toList();
    }

    public List<String> findAvailableVisitTimesInIntervals(List<Long> serviceTypeIds) {
        List<ServiceTypeDto> serviceTypes = userClient.getServiceTypes(serviceTypeIds);
        Long doctorId = getDoctorIdOrThrow(serviceTypes, serviceTypeIds);
        List<CalendarDayEntity> calendarDayEntities = calendarDayRepository.findCalendarDaysByDoctorId(doctorId);

        List<Long> allDoctorServiceTypeIds = userClient.getServiceTypesByDoctorId(doctorId).stream().map(ServiceTypeDto::getId).toList();
        Map<CalendarDayEntity, List<VisitEntity>> visitsToCalendarDay = fillVisitsToDay(calendarDayEntities, allDoctorServiceTypeIds);
        Map<CalendarDayEntity, List<StartEndTime>> visitsTimeToCalendarDay = fillVisitTimesToDay(visitsToCalendarDay);
        Time visitDuration = calculateVisitDuration(serviceTypes);

        List<StartEndTime> possibleVisitTimes =
                getAvailableVisitTimes(visitsTimeToCalendarDay).stream()
                        .filter(visitTime -> isEnoughTimeForVisit(visitTime, visitDuration))
                        .toList();

        return getVisitStartDatesInIntervals(possibleVisitTimes, visitDuration);
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

    private Page<VisitEntity> findUpcomingVisitsByRequest(VisitSearchRequestDto searchRequestDto) {
        return visitRepository.findAll(
                new VisitSpecification(searchRequestDto.getUserId(), true),
                searchRequestDto.pageable());
    }

    private boolean isApprovalPossible(Timestamp reservationDate) {
        long currentTimeMs = System.currentTimeMillis();
        long reservationTimeMs = reservationDate.getTime();

        return (currentTimeMs - reservationTimeMs) < VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS;
    }

    public void validateStartDateOrThrow(List<Long> serviceTypeIds, String startDate) {
        TimestampFormatParser.parseOrThrow(startDate);

        boolean isStartDateCorrect =
                findAvailableVisitTimesInIntervals(serviceTypeIds).stream()
                        .anyMatch(time -> time.equals(startDate));

        if (!isStartDateCorrect) {
            throw new IncorrectValueException(String.format(INCORRECT_START_DATE_MESSAGE, startDate));
        }
    }

    private Long getDoctorIdOrThrow(List<ServiceTypeDto> serviceTypes, List<Long> serviceTypeIds) {
        return serviceTypes.stream().map(ServiceTypeDto::getDoctorId).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(DOCTOR_NOT_FOUND_MESSAGE, serviceTypeIds)));
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

                                    Timestamp end = getWorkEnd(start, totalTime);

                                    return new StartEndTime(start, end);
                                })
                                .toList()
                ));
    }

    private Time calculateVisitDuration(List<ServiceTypeDto> serviceTypes) {
        return serviceTypes.stream()
                .map(time -> Time.valueOf(time.getDurationTime()))
                .reduce((time1, time2) -> new Time(time1.getTime() + time2.getTime()))
                .orElse(Time.valueOf("00:00:00"));
    }

    private long calculateVisitDuration(Time visitDuration) {
        int hours = visitDuration.getHours();
        int minutes = visitDuration.getMinutes();
        return (hours * ONE_HOUR_IN_MS) + (minutes * ONE_MINUTE_IN_MS);
    }

    private List<StartEndTime> getAvailableVisitTimes(Map<CalendarDayEntity, List<StartEndTime>> visitsTimeToCalendarDay) {
        List<StartEndTime> availableVisitTimes = new ArrayList<>();

        visitsTimeToCalendarDay.forEach((day, visitTimes) -> {
            Timestamp workStart = day.getStartDate();
            Timestamp workEnd = getWorkEnd(workStart, day.getWorkDuration());

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

    private Timestamp getWorkEnd(Timestamp startDate, Time time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate.getTime());
        cal.add(Calendar.HOUR, time.getHours());
        cal.add(Calendar.MINUTE, time.getMinutes());
        return new Timestamp(cal.getTime().getTime());
    }

    private boolean isEnoughTimeForVisit(StartEndTime startEndTime, Time totalTime) {
        long difference = calculateTimeDifference(startEndTime);
        long total = totalTime.getTime();

        return difference > total;
    }

    private List<String> getVisitStartDatesInIntervals(List<StartEndTime> possibleVisitTimes, Time visitDuration) {
        List<String> visitStartDates = new ArrayList<>();

        possibleVisitTimes.forEach(visitTime -> {
            long difference = calculateTimeDifference(visitTime);
            long duration = calculateVisitDuration(visitDuration);
            long intervals = (difference - duration) / VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MS;

            for (int i = 0; i <= intervals; i++) {
                Timestamp startDate = getNextInterval(visitTime.getStartTime(), i * VISIT_APPROVAL_AND_INTERVAL_TIME_IN_MINUTES);
                String startDateString = TimestampFormatParser.parse(startDate);
                visitStartDates.add(startDateString);
            }
        });

        return visitStartDates;
    }

    private long calculateTimeDifference(StartEndTime startEndTime) {
        long start = startEndTime.getStartTime().getTime();
        long end = startEndTime.getEndTime().getTime();
        return end - start;
    }

    private Timestamp getNextInterval(Timestamp startDate, int timeLaterInMs) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate.getTime());
        cal.add(Calendar.MINUTE, timeLaterInMs);
        return new Timestamp(cal.getTime().getTime());
    }
}
