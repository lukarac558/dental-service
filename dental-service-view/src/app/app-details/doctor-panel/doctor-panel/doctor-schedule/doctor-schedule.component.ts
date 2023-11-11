import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CalendarEvent, CalendarView } from 'angular-calendar';
import { getMonth, getYear, isAfter, isSameDay, isSameMonth } from 'date-fns';
import { BehaviorSubject, filter, map, Observable, Subject, switchMap } from 'rxjs';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import { UsersService } from 'src/app/core/services/users.service';
import { eventColor } from 'src/app/shared/constants/calendar.constants';

import { AddWorkingDayModalComponent } from './add-schedule-modal/add-schedule-modal.component';

@Component({
    selector: 'ds-doctor-schedule',
    templateUrl: './doctor-schedule.component.html',
    styleUrls: ['./doctor-schedule.component.scss'],
    providers: [DatePipe],
})
export class DoctorScheduleComponent implements OnInit {
    CalendarView = CalendarView;
    viewDate: Date = new Date();
    activeDayIsOpen = false;
    refresh = new Subject<void>();
    events: CalendarEvent[] = [];
    events$: Observable<CalendarEvent[]>;
    currentDate$ = new BehaviorSubject<{ month: number, year: number }>({
        month: getMonth(this.viewDate),
        year: getYear(this.viewDate)
    });

    readonly view: CalendarView = CalendarView.Month;

    constructor(
        private _doctorsService: DoctorsService,
        private _datePipe: DatePipe,
        private _dialog: MatDialog,
        private _usersService: UsersService
    ) { }

    ngOnInit(): void {
        this.events$ = this.currentDate$.pipe(
            switchMap(_ => this._usersService.getCurrentUserDetails()),
            switchMap(user => this._doctorsService.getCurrentDoctorSchedule(user.id as number)),
            map(doctorSchedule => {
                return doctorSchedule.map(schedule => {
                    const startTime = this._datePipe.transform(schedule.startDate, 'HH:mm');
                    const workDuration = schedule.workDuration.substring(0, 5);
                    return {
                        start: new Date(schedule.startDate),
                        title: `Rozpoczęcie pracy: ${startTime}, czas pracy: ${workDuration}`,
                        color: { ...eventColor },
                        allDay: true
                    } as CalendarEvent
                })
            })
        );
    }

    dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
        if (isSameMonth(date, this.viewDate)) {
            if (
                (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
                events.length === 0
            ) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
            }

            this.viewDate = date;

            if (events.length === 0 && isAfter(this.viewDate, new Date())) {
                this.addWorkingDay(this.viewDate);
            }
        }
    }

    closeOpenMonthViewDay() {
        this.currentDate$.next({
            month: getMonth(this.viewDate),
            year: getYear(this.viewDate)
        });
        this.activeDayIsOpen = false;
    }

    private addWorkingDay(date: Date): void {
        this._dialog.open(AddWorkingDayModalComponent, {
            width: '450px',
            data: {
                date: date,
            },
        }).afterClosed().pipe(
            filter(result => result)
        ).subscribe(_ => {
            this.currentDate$.next(this.currentDate$.value);
        });
    }
}
