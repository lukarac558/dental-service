import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { catchError, map, Observable, of, switchMap } from 'rxjs';
import { appConfig } from 'src/app/app.config';

import {
    Doctor,
    DoctorAddSchedule,
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

    getCurrentDoctorSchedule(doctorId: number): Observable<DoctorSchedule[]> {
        return this._http.get<DoctorSchedule[]>(`${appConfig.apiUrl}/reservation/calendar-days/user/${doctorId}`)
    }

    addCurrentDoctorSchedule(schedule: DoctorAddSchedule): Observable<void> {
        return this._http.post<void>(`${appConfig.apiUrl}/reservation/calendar-days`, {
            ...schedule
        });
    }

    getCurrentDoctorCompetency(): Observable<DoctorCompetency> {
        return this._http.get<DoctorCompetency>(`${appConfig.apiUrl}/user/competency-informations`).pipe(
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
        return this._http.post<DoctorService[]>(`${appConfig.apiUrl}/user/service-types/all`, {
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
        return this._http.post<void>(`${appConfig.apiUrl}/user/service-types`, {
            ...service
        });
    }

    updateCurrentDoctorService(id: number, service: DoctorServiceForm): Observable<void> {
        return this._http.put<void>(`${appConfig.apiUrl}/user/service-types/${id}`, {
            ...service
        });
    }

    deleteCurrentDoctorService(id: number): Observable<void> {
        return this._http.delete<void>(`${appConfig.apiUrl}/user/service-types/${id}`);
    }

    getDoctors(searchCriteria: CustomPageCriteria<DoctorSearch>): Observable<Page<DoctorShort>> {
        return this._http.post<Page<DoctorShort>>(`${appConfig.apiUrl}/user/users/doctors`, {
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
        return this._http.post<Doctor>(`${appConfig.apiUrl}/user/users/doctor/${id}`, {});
    }

    getDoctorAvailableDays(serviceIds: number[]): Observable<VisitAvailableDate[]> {
        return this._http.get<string[]>(`${appConfig.apiUrl}/reservation/visits/available-times/${serviceIds.join(',')}`).pipe(
            map(result => {
                return result.reduce((group: { date: string, hours: string[] }[], dateAndTime) => {
                    const [date, time] = dateAndTime.split(' ');
                    const index = group.findIndex(v => v.date == date);
                    if (index >= 0) {
                        group[index].hours.push(time);
                    } else {
                        group.push({ date: date, hours: [time] });
                    }
                    return group;
                }, [])
            }),
            map(result => {
                return result.map(day => {
                    return {
                        date: moment(day.date).utc(true),
                        hours: day.hours.map(hour => hour.substring(0, 5))
                    } as VisitAvailableDate;
                });
            })
        );
    }

    private addDoctorCompetency(doctorCompetency: DoctorCompetencyForm): Observable<void> {
        return this._http.post<void>(`${appConfig.apiUrl}/user/competency-informations`, {
            ...doctorCompetency
        });
    }

    private updateDoctorCompetency(doctorCompetency: DoctorCompetencyForm): Observable<void> {
        return this._http.put<void>(`${appConfig.apiUrl}/user/competency-informations`, {
            ...doctorCompetency
        });
    }
}
