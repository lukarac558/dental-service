import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { User } from '../models/user.model';


@Injectable({
    providedIn: 'root'
})
export class UsersService {

    constructor(private _http: HttpClient) { }

    getUser(): Observable<User> {
        return of({
            id: 1,
            name: 'Jan Nowak'
        });
    }
}
