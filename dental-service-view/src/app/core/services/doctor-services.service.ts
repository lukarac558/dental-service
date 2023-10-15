import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { Lookup } from '../models/lookup.model';

@Injectable({
    providedIn: 'root'
})
export class DoctorServicesService {
    constructor(private _http: HttpClient) { }

    // PROBABLY CACHE
    getDoctorServices(): Observable<Lookup[]> {
        return of(
            [{
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
            }, {
                id: 6,
                name: 'Konsultacja ortodontyczna'
            }]
        )
    }
}
