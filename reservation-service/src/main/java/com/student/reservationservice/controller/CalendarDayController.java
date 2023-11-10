package com.student.reservationservice.controller;

import com.student.api.dto.reservation.CalendarDayDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimeFormatParser;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.entity.CalendarDay;
import com.student.reservationservice.service.CalendarDayService;
import com.student.reservationservice.user.UserClient;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/calendar-days")
@Tag(name = "Calendar days")
@RequiredArgsConstructor
public class CalendarDayController {
    private final ModelMapper modelMapper;
    private final CalendarDayService calendarDayService;
    private final UserClient userClient;


    @GetMapping("/{id}")
    @Operation(summary = "Find calendar day by id")
    @ApiResponse(responseCode = "404", description = "Calendar day not found.")
    public ResponseEntity<CalendarDayDto> getCalendarDayById(@PathVariable("id") Long id) {
        CalendarDay calendarDay = calendarDayService.findCalendarDayById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CALENDAR_DAY_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(calendarDay, CalendarDayDto.class), HttpStatus.OK);
    }

    @GetMapping("/user/{user_id}")
    @Operation(summary = "Find all calendar days associated with a doctor by his id.")
    public ResponseEntity<List<CalendarDayDto>> getCalendarDaysByUserId(@PathVariable("user_id") Long userId) {
        List<CalendarDay> calendarDays = calendarDayService.findCalendarDaysByUserId(userId);
        List<CalendarDayDto> calendarDaysResponse = calendarDays.stream().map(day -> modelMapper.map(day, CalendarDayDto.class)).toList();
        return new ResponseEntity<>(calendarDaysResponse, HttpStatus.OK);
    }

    @PostMapping("")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "422", description = "Incorrect time or timestamp format is given")
    @Operation(summary = "Add new calendar day for given doctor.")
    public ResponseEntity<CalendarDayDto> addCalendarDay(@RequestBody CalendarDayDto calendarDayDto) {
        Long userId = calendarDayDto.getDoctorId();
        UserPersonalDetailsDto user = userClient.getUserById(userId);

        CalendarDay calendarDay = calendarDayService.addOrUpdateCalendarDay(mapToCalendarDay(calendarDayDto, user.getId()));

        CalendarDayDto calendarDayDTO = mapToCalendarDayDTO(calendarDay, user.getId());
        return new ResponseEntity<>(calendarDayDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete calendar day by id.")
    public ResponseEntity<?> deleteCalendarDay(@PathVariable("id") Long id) {
        calendarDayService.deleteCalendarDay(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private CalendarDay mapToCalendarDay(CalendarDayDto calendarDayDto, Long doctorId) {
        CalendarDay calendarDay = new CalendarDay();
        setTimesOrThrow(calendarDay, calendarDayDto.getStartDate(), calendarDayDto.getWorkDuration());
        calendarDay.setDoctorId(doctorId);
        return calendarDay;
    }

    private void setTimesOrThrow(CalendarDay calendarDay, String startDate, String workDuration) {
        calendarDay.setStartDate(TimestampFormatParser.parseOrThrow(startDate));
        calendarDay.setWorkDuration(TimeFormatParser.parse(workDuration));
    }

    private CalendarDayDto mapToCalendarDayDTO(CalendarDay calendarDay, Long doctorId) {
        CalendarDayDto calendarDayDTO = modelMapper.map(calendarDay, CalendarDayDto.class);
        calendarDayDTO.setDoctorId(doctorId);
        return calendarDayDTO;
    }
}
