import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, EMPTY, Observable, of, startWith } from 'rxjs';
import { appConfig } from 'src/app/app.config';

import { UserDetails, UserDetailsForm } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    constructor(private _http: HttpClient) { }

    getUserDetails(): Observable<UserDetails> {
        return of({
            firstName: 'Jan',
            lastName: 'Nowak',
            email: 'jannowak@gmail.com',
            phoneNumber: '543321567',
            zipCode: '44-100',
            city: 'Gliwice',
            address: 'Kujawska 4/1',
            gender: 1,
            role: 2
        }).pipe(delay(500));
    }

    updateUserDetails(userDetails: UserDetailsForm): Observable<void> {
        return EMPTY.pipe(startWith(undefined), delay(500));
    }

    getCurrentUserDetails(): Observable<any> {
        return this._http.get(`${appConfig.apiUrl}/user/user`);
    }
}
