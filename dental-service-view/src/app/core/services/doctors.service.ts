import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, EMPTY, Observable, of, startWith } from 'rxjs';

import {
    Doctor,
    DoctorAddScheduleForm,
    DoctorInfo,
    DoctorInfoForm,
    DoctorSchedule,
    DoctorSearch,
    DoctorShort
} from '../models/doctor.model';
import { Gender } from '../models/gender.model';
import { CustomPageCriteria, Page } from '../models/page.model';

@Injectable({
    providedIn: 'root'
})
export class DoctorsService {
    constructor(private _http: HttpClient) { }

    getCurrentDoctorInfo(): Observable<DoctorInfo> {
        return of({
            specialization: 'Stomatolog',
            aboutMe: 'Absolwent Śląskiego Uniwersytetu Medycznego w Katowicach. Specjalizuje się w leczeniu zachowawczym i protetycznym, ale wykonuje również zabiegi z zakresu endodoncji, chirurgii stomatologicznej oraz stomatologii dziecięcej. Poza gabinetem miłośnik gór i pływania.',
            services: [{
                id: 1,
                name: 'Konsultacja stomatologiczna'
            }, {
                id: 2,
                name: 'Wybielanie zębów'
            }, {
                id: 3,
                name: 'Ekstrakcja zęba'
            }, {
                id: 4,
                name: 'Leczenie próchnicy'
            }, {
                id: 5,
                name: 'Pakiet higienizacyjny'
            }]
        }).pipe(delay(500));
    }

    getCurrentDoctorSchedule(month: number, year: number): Observable<DoctorSchedule[]> {
        return of([{
            startDate: new Date(2023, 9, 24, 8, 0),
            workDuration: "8:00",
            startBreakTime: "12:00",
            breakDuration: "0:30"
        }, {
            startDate: new Date(2023, 9, 25, 8, 0),
            workDuration: "7:00",
            startBreakTime: "12:00",
            breakDuration: "0:30"
        }, {
            startDate: new Date(2023, 9, 27, 8, 0),
            workDuration: "6:00",
            startBreakTime: "12:00",
            breakDuration: "0:30"
        }]).pipe(delay(500));
    }

    addCurrentDoctorSchedule(schedule: DoctorAddScheduleForm): Observable<void> {
        return EMPTY.pipe(startWith(undefined), delay(500));
    }

    updateCurrentDoctorInfo(userDetails: DoctorInfoForm): Observable<void> {
        return EMPTY.pipe(startWith(undefined), delay(500));
    }

    getDoctors(searchCriteria: CustomPageCriteria<DoctorSearch>): Observable<Page<DoctorShort>> {
        return of({
            itemsCount: 6,
            items: [{
                id: 1,
                firstName: 'Alan',
                lastName: 'Kwieciński',
                gender: Gender.Male,
                specialization: 'Stomatolog',
                aboutMe: 'Absolwent Śląskiego Uniwersytetu Medycznego w Katowicach. Specjalizuje się w leczeniu zachowawczym i protetycznym, ale wykonuje również zabiegi z zakresu endodoncji, chirurgii stomatologicznej oraz stomatologii dziecięcej. Poza gabinetem miłośnik gór i pływania.',
            }, {
                id: 2,
                firstName: 'Agata',
                lastName: 'Fąk',
                gender: Gender.Female,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
            }, {
                id: 3,
                firstName: 'Michał',
                lastName: 'Baron',
                gender: Gender.Male,
                specialization: 'Ortodonta',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
            }, {
                id: 4,
                firstName: 'Agnieszka',
                lastName: 'Podlaska',
                gender: Gender.Female,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
            }, {
                id: 5,
                firstName: 'Maciek',
                lastName: 'Macalski',
                gender: Gender.Male,
                specialization: 'Stomatolog',
                aboutMe: 'Lorem ipsum dolor sit amet consectetur, adipisicing elit. Ratione, ipsum quia ea minima quidem sint tempore, nesciunt fugit optio voluptatum molestiae eum sit commodi, autem asperiores. Fugit laudantium voluptates quas.',
            }, {
                id: 6,
                firstName: 'Katarzyna',
                lastName: 'Szczecińska',
                gender: Gender.Female,
                specialization: 'Stomatolog',
                aboutMe: '',
            }]
        }).pipe(delay(500));
    }

    getDoctor(id: number): Observable<Doctor> {
        return of({
            id: 1,
            firstName: 'Alan',
            lastName: 'Kwieciński',
            gender: Gender.Male,
            specialization: 'Stomatolog',
            aboutMe: 'Absolwent Śląskiego Uniwersytetu Medycznego w Katowicach. Specjalizuje się w leczeniu zachowawczym i protetycznym, ale wykonuje również zabiegi z zakresu endodoncji, chirurgii stomatologicznej oraz stomatologii dziecięcej. Poza gabinetem miłośnik gór i pływania.',
            services: [{
                id: 1,
                name: 'Konsultacja stomatologiczna'
            }, {
                id: 2,
                name: 'Wybielanie zębów'
            }, {
                id: 3,
                name: 'Ekstrakcja zęba'
            }, {
                id: 4,
                name: 'Leczenie próchnicy'
            }, {
                id: 5,
                name: 'Pakiet higienizacyjny'
            }]
        }).pipe(delay(500));
    }
}
