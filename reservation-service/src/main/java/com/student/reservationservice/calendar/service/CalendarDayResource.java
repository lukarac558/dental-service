package com.student.reservationservice.calendar.service;

import com.student.api.ApplicationUserInfoDTO;
import com.student.api.CalendarDayCreationDTO;
import com.student.api.CalendarDayDTO;
import com.student.reservationservice.calendar.entity.CalendarDay;
import com.student.reservationservice.calendar.exception.CalendarDayNotFoundException;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.exception.UserNotFoundException;
import com.student.reservationservice.user.applicationuser.service.UserService;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/calendar-day")
public class CalendarDayResource {
    private final ModelMapper modelMapper;
    private final CalendarDayService calendarDayService;
    private final UserService userService;

    @Autowired
    public CalendarDayResource(ModelMapper modelMapper, CalendarDayService calendarDayService, UserService userService) {
        this.modelMapper = modelMapper;
        this.calendarDayService = calendarDayService;
        this.userService = userService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CalendarDayDTO> getCalendarDayById(@PathVariable("id") Long id) {
        CalendarDay calendarDay = calendarDayService.findCalendarDayById(id)
                .orElseThrow(() -> new CalendarDayNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(calendarDay, CalendarDayDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by/{user_id}")
    public ResponseEntity<List<CalendarDayDTO>> getCalendarDaysByUserId(@PathVariable("user_id") Long userId) {
        List<CalendarDay> calendarDays = calendarDayService.findCalendarDaysByUserId(userId);
        List<CalendarDayDTO> calendarDaysResponse = calendarDays.stream().map(day -> modelMapper.map(day, CalendarDayDTO.class)).toList();
        return new ResponseEntity<>(calendarDaysResponse, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CalendarDayDTO> addCalendarDay(@RequestBody CalendarDayCreationDTO calendarDayCreationDTO) {
        Long userId = calendarDayCreationDTO.getUserId();
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        CalendarDay calendarDay = calendarDayService.addOrUpdateCalendarDay(mapToCalendarDay(calendarDayCreationDTO, user));

        CalendarDayDTO calendarDayDTO = mapToCalendarDayDTO(calendarDay, user);
        return new ResponseEntity<>(calendarDayDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CalendarDayDTO> updateType(@PathVariable("id") Long id,
                                                     @ApiParam(value = "Work start time", example = "2023-10-27 08:00:00")
                                                     @RequestParam String startDate,
                                                     @ApiParam(value = "Work duration", example = "08:00:00")
                                                     @RequestParam String workDuration,
                                                     @ApiParam(value = "Break start time", example = "12:00:00")
                                                     @RequestParam String startBreakTime,
                                                     @ApiParam(value = "Break duration", example = "01:00:00")
                                                     @RequestParam String breakDuration) {
        CalendarDay calendarDay = calendarDayService.findCalendarDayById(id)
                .map(c -> {
                    c.setStartDate(Timestamp.valueOf(startDate));
                    c.setWorkDuration(Time.valueOf(workDuration));
                    c.setStartBreakTime(Time.valueOf(startBreakTime));
                    c.setBreakDuration(Time.valueOf(breakDuration));
                    return c;
                })
                .orElseThrow(() -> new CalendarDayNotFoundException(id));

        CalendarDay updatedCalendarDay = calendarDayService.addOrUpdateCalendarDay(calendarDay);
        return new ResponseEntity<>(modelMapper.map(updatedCalendarDay, CalendarDayDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCalendarDay(@PathVariable("id") Long id) {
        calendarDayService.deleteCalendarDay(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private CalendarDay mapToCalendarDay(CalendarDayCreationDTO calendarDayCreationDTO, ApplicationUser user) {
        CalendarDay calendarDay = modelMapper.map(calendarDayCreationDTO, CalendarDay.class);
        calendarDay.setId(null);
        calendarDay.setUser(user);
        return calendarDay;
    }

    private CalendarDayDTO mapToCalendarDayDTO(CalendarDay calendarDay, ApplicationUser applicationUser) {
        CalendarDayDTO calendarDayDTO = modelMapper.map(calendarDay, CalendarDayDTO.class);
        ApplicationUserInfoDTO applicationUserInfoDTO = new ApplicationUserInfoDTO(
                applicationUser.getId(),
                applicationUser.getEmail(),
                applicationUser.getName(),
                applicationUser.getSurname(),
                applicationUser.getPhoneNumber()
        );

        calendarDayDTO.setUser(applicationUserInfoDTO);
        return calendarDayDTO;
    }
}
