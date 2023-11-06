import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, EMPTY, Observable, of, startWith } from 'rxjs';

import { Page, PageCriteria } from '../models/page.model';
import { ReservationVisit, Visit, VisitForm } from '../models/visits.model';

@Injectable({
    providedIn: 'root'
})
export class VisitsService {
    constructor(private _http: HttpClient) { }

    getVisits(searchCriteria: PageCriteria): Observable<Page<Visit>> {
        return of({
            itemsCount: 2,
            items: [{
                id: 2,
                startDate: new Date(2023, 10, 15, 12, 0, 0),
                endDate: new Date(2023, 10, 15, 14, 0, 0),
                doctorName: 'Alan Kwieciński',
                doctorGender: 'MALE',
                doctorSpecialization: 'Stomatolog',
                services: [{
                    id: 1,
                    name: 'Konsultacja stomatologiczna'
                }, {
                    id: 4,
                    name: 'Leczenie próchnicy'
                }]
            }, {
                id: 1,
                startDate: new Date(2023, 9, 21, 8, 0, 0),
                endDate: new Date(2023, 9, 21, 9, 0, 0),
                doctorName: 'Agata Fąk',
                doctorGender: 'MALE',
                doctorSpecialization: 'Stomatolog',
                services: [{
                    id: 5,
                    name: 'Pakiet higienizacyjny'
                }]
            }]
        }).pipe(delay(500));
    }

    addVisit(visitForm: VisitForm): Observable<void> {
        return EMPTY.pipe(startWith(undefined), delay(500));
    }

    getReservedVisits(): Observable<ReservationVisit[]> {
        return of([{
            id: 3,
            startDate: new Date(2023, 10, 28, 12, 0, 0),
            endDate: new Date(2023, 10, 28, 14, 0, 0),
            doctorName: 'Alan Kwieciński',
            doctorGender: 'MALE',
            doctorSpecialization: 'Stomatolog',
            reservationEndDate: new Date(2023, 10, 1, 20, 0, 0),
            services: [{
                id: 5,
                name: 'Pakiet higienizacyjny'
            }]
        }]).pipe(delay(1000));
    }

    confirmVisit(id: number): Observable<void> {
        return EMPTY.pipe(startWith(undefined), delay(500));
    }
}
