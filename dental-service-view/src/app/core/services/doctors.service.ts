import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, delay, EMPTY, map, Observable, of, startWith, switchMap } from 'rxjs';
import { appConfig } from 'src/app/app.config';

import {
    Doctor,
    DoctorAddScheduleForm,
    DoctorCompetency,
    DoctorCompetencyForm,
    DoctorSchedule,
    DoctorSearch,
    DoctorService,
    DoctorServiceForm,
    DoctorShort
} from '../models/doctor.model';
import { Gender } from '../models/gender.model';
import { CustomPageCriteria, Page } from '../models/page.model';
import { VisitAvailableDate } from '../models/visits.model';

@Injectable({
    providedIn: 'root'
})
export class DoctorsService {
    constructor(private _http: HttpClient) { }

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

    getCurrentDoctorCompetency(): Observable<DoctorCompetency> {
        return this._http.get<DoctorCompetency>(`${appConfig.apiUrl}/user/competency-information`).pipe(
            catchError((_error: HttpErrorResponse) => {
                return of({
                    id: null,
                    title: '',
                    description: '',
                } as DoctorCompetency);
            }),
        );
    }

    updateCurrentDoctorCompetency(doctorCompetency: DoctorCompetencyForm): Observable<void> {
        return of(doctorCompetency.id).pipe(
            switchMap(id => {
                if (!!id) {
                    return this.updateDoctorCompetency(doctorCompetency);
                } else {
                    return this.addDoctorCompetency(doctorCompetency);
                }
            })
        );
    }

    getCurrentDoctorServices(): Observable<DoctorService[]> {
        return this._http.post<DoctorService[]>(`${appConfig.apiUrl}/user/service-type/all`, {
            page: 0,
            pageSize: 1,
            enabled: false,
            showOnlyYour: true,
            name: ""
        }).pipe(
            map(result => (result as any).content)
        );
    }

    addCurrentDoctorService(service: DoctorServiceForm): Observable<void> {
        return this._http.post<void>(`${appConfig.apiUrl}/user/service-type`, {
            ...service
        });
    }

    updateCurrentDoctorService(id: number, service: DoctorServiceForm): Observable<void> {
        return this._http.put<void>(`${appConfig.apiUrl}/user/service-type/${id}`, {
            ...service
        });
    }

    deleteCurrentDoctorService(id: number): Observable<void> {
        return this._http.delete<void>(`${appConfig.apiUrl}/user/service-type/${id}`);
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

    getDoctorAvailableDays(id: number, serviceIds: number[]): Observable<VisitAvailableDate[]> {
        return of([{
            date: new Date(Date.UTC(2023, 10, 21)),
            hours: ["12:00", "12:30",]
        }, {
            date: new Date(Date.UTC(2023, 10, 22)),
            hours: ["12:00", "12:30", "13:00", "13:30", "14:00"]
        }, {
            date: new Date(Date.UTC(2023, 10, 23)),
            hours: ["12:00", "12:30", "13:00", "13:30", "14:00"]
        }, {
            date: new Date(Date.UTC(2023, 10, 24)),
            hours: ["13:00", "13:30", "14:00"]
        }]).pipe(delay(500));
    }

    private addDoctorCompetency(doctorCompetency: DoctorCompetencyForm): Observable<void> {
        return this._http.post<void>(`${appConfig.apiUrl}/user/competency-information`, {
            ...doctorCompetency
        });
    }

    private updateDoctorCompetency(doctorCompetency: DoctorCompetencyForm): Observable<void> {
        return this._http.put<void>(`${appConfig.apiUrl}/user/competency-information`, {
            ...doctorCompetency
        });
    }
}
