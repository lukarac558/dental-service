import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, Observable, of } from 'rxjs';

import { UserDetails } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class UsersService {

    constructor(private _http: HttpClient) { }

    getUserDetails(): Observable<UserDetails> {
        return of({
            id: 1,
            firstName: 'Jan',
            lastName: 'Nowak',
            email: 'jannowak@gmail.com',
            phoneNumber: '543321567',
            zipCode: '44-100',
            city: 'Gliwice',
            address: 'Kujawska 4/1'
        }).pipe(delay(500));
    }
}
