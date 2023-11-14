import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { map, Observable } from 'rxjs';
import { appConfig } from 'src/app/app.config';

import { Page, PageCriteria } from '../models/page.model';
import { Visit, VisitForm } from '../models/visits.model';

@Injectable({
    providedIn: 'root'
})
export class VisitsService {
    constructor(private _http: HttpClient) { }

    getVisits(patientId: number, searchCriteria: PageCriteria): Observable<Page<Visit>> {
        return this._http.post<Page<Visit>>(`${appConfig.apiUrl}/reservation/visits/approved`, {
            ...searchCriteria,
            userId: patientId
        }).pipe(
            map((result: any) => {
                return {
                    items: result.content,
                    itemsCount: result.totalElements
                }
            })
        );
    }

    addVisit(patientId: number, visitForm: VisitForm): Observable<void> {
        const time = visitForm.startHour.split(':');
        return this._http.post<void>(`${appConfig.apiUrl}/reservation/visits`, {
            visit: {
                startDate: moment(visitForm.date).hour(+time[0]).minute(+time[1]).format("YYYY-MM-DD HH:mm:ss"),
                patientId: patientId,
                description: ''
            },
            serviceTypeIds: visitForm.serviceIds
        });
    }

    getReservedVisits(patientId: number): Observable<Visit[]> {
        return this._http.post<Visit[]>(`${appConfig.apiUrl}/reservation/visits/not-approved`, {
            userId: patientId,
            enabled: false
        }).pipe(
            map((result: any) => result.content)
        )
    }

    confirmVisit(id: number): Observable<void> {
        return this._http.put<void>(`${appConfig.apiUrl}/reservation/visits/approve/${id}`, {});
    }
}
