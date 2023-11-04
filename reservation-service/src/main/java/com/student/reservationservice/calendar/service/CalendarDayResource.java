package com.student.reservationservice.calendar.service;

import com.student.api.dto.reservation.CalendarDayCreationDTO;
import com.student.api.dto.reservation.CalendarDayDTO;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.reservationservice.calendar.entity.CalendarDay;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimeFormatParser;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.user.UserClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.student.api.exception.handler.ErrorConstants.CALENDAR_DAY_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/calendar-day")
@Tag(name = "Calendar day")
@RequiredArgsConstructor
public class CalendarDayResource {
    private final ModelMapper modelMapper;
    private final CalendarDayService calendarDayService;
    private final UserClient userClient;


    @GetMapping("/{id}")
    @Operation(summary = "Find calendar day by id")
    @ApiResponse(responseCode = "404", description = "Calendar day not found.")
    public ResponseEntity<CalendarDayDTO> getCalendarDayById(@PathVariable("id") Long id) {
        CalendarDay calendarDay = calendarDayService.findCalendarDayById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CALENDAR_DAY_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(calendarDay, CalendarDayDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by/{user_id}")
    @Operation(summary = "Find all calendar days associated with a doctor by his id.")
    public ResponseEntity<List<CalendarDayDTO>> getCalendarDaysByUserId(@PathVariable("user_id") Long userId) {
        List<CalendarDay> calendarDays = calendarDayService.findCalendarDaysByUserId(userId);
        List<CalendarDayDTO> calendarDaysResponse = calendarDays.stream().map(day -> modelMapper.map(day, CalendarDayDTO.class)).toList();
        return new ResponseEntity<>(calendarDaysResponse, HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "422", description = "Incorrect time or timestamp format is given")
    @Operation(summary = "Add new calendar day for given doctor.")
    public ResponseEntity<CalendarDayDTO> addCalendarDay(@RequestBody CalendarDayCreationDTO calendarDayCreationDTO) {
        Long userId = calendarDayCreationDTO.getDoctorId();
        UserPersonalDetailsDto user = userClient.getUserById(userId);

        CalendarDay calendarDay = calendarDayService.addOrUpdateCalendarDay(mapToCalendarDay(calendarDayCreationDTO, user.getId()));

        CalendarDayDTO calendarDayDTO = mapToCalendarDayDTO(calendarDay, user.getId());
        return new ResponseEntity<>(calendarDayDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Calendar day not found")
    @ApiResponse(responseCode = "422", description = "Incorrect time or timestamp format is given")
    @Operation(summary = "Update calendar day by id.")
    public ResponseEntity<CalendarDayDTO> updateCalendarDay(@PathVariable("id") Long id,
                                                            @Parameter(required = true, example = "2023-10-27 08:00:00")
                                                            @RequestParam String startDate,
                                                            @Parameter(required = true, example = "08:00:00")
                                                            @RequestParam String workDuration,
                                                            @Parameter(example = "12:00:00")
                                                            @RequestParam String startBreakTime,
                                                            @Parameter(example = "01:00:00")
                                                            @RequestParam String breakDuration) {
        CalendarDay calendarDay = calendarDayService.findCalendarDayById(id)
                .map(c -> {
                    setTimesOrThrow(c, startDate, workDuration, startBreakTime, breakDuration);
                    return c;
                })
                .orElseThrow(() -> new NotFoundException(String.format(CALENDAR_DAY_NOT_FOUND_MESSAGE, id)));

        CalendarDay updatedCalendarDay = calendarDayService.addOrUpdateCalendarDay(calendarDay);
        return new ResponseEntity<>(modelMapper.map(updatedCalendarDay, CalendarDayDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete calendar day by id.")
    public ResponseEntity<?> deleteCalendarDay(@PathVariable("id") Long id) {
        calendarDayService.deleteCalendarDay(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private CalendarDay mapToCalendarDay(CalendarDayCreationDTO calendarDayCreationDTO, Long doctorId) {
        CalendarDay calendarDay = new CalendarDay();
        setTimesOrThrow(calendarDay, calendarDayCreationDTO.getStartDate(), calendarDayCreationDTO.getWorkDuration(),
                calendarDayCreationDTO.getStartBreakTime(), calendarDayCreationDTO.getBreakDuration());
        calendarDay.setDoctorId(doctorId);
        return calendarDay;
    }

    private void setTimesOrThrow(CalendarDay calendarDay, String startDate, String workDuration, String startBreakTime, String breakDuration) {
        calendarDay.setStartDate(TimestampFormatParser.parse(startDate));
        calendarDay.setWorkDuration(TimeFormatParser.parse(workDuration));

        if (!startBreakTime.isBlank()) {
            calendarDay.setStartBreakTime(TimeFormatParser.parse(startBreakTime));
        } else {
            calendarDay.setStartBreakTime(null);
        }

        if (!breakDuration.isBlank()) {
            calendarDay.setBreakDuration(TimeFormatParser.parse(breakDuration));
        } else {
            calendarDay.setBreakDuration(null);
        }
    }

    private CalendarDayDTO mapToCalendarDayDTO(CalendarDay calendarDay, Long doctorId) {
        CalendarDayDTO calendarDayDTO = modelMapper.map(calendarDay, CalendarDayDTO.class);
        calendarDayDTO.setDoctorId(doctorId);
        return calendarDayDTO;
    }
}
