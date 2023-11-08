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
        return this._http.post<Page<DoctorShort>>(`${appConfig.apiUrl}/user/user/doctor/all`, {
            ...searchCriteria
        }).pipe(
            map((result: any) => {
                return {
                    itemsCount: result.totalElements,
                    items: result.content
                }
            })
        );
    }

    getDoctor(id: number): Observable<Doctor> {
        return this._http.post<Doctor>(`${appConfig.apiUrl}/user/user/doctor/${id}`, {});
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
