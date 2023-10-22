import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, Observable, of } from 'rxjs';

import { Page, PageCriteria } from '../models/page.model';
import { Visit } from '../models/visits.model';

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
                startDate: new Date(2023, 9, 28, 12, 0, 0),
                endDate: new Date(2023, 9, 28, 14, 0, 0),
                doctorName: 'Alan Kwieciński',
                doctorGender: 1,
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
                doctorGender: 2,
                doctorSpecialization: 'Stomatolog',
                services: [{
                    id: 5,
                    name: 'Pakiet higienizacyjny'
                }]
            }]
        }).pipe(delay(500))
    }
}
