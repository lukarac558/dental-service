import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, switchMap, tap } from 'rxjs';
import { appConfig } from 'src/app/app.config';

import { Gender } from '../models/gender.model';
import { UserDetails, UserDetailsForm } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    private _userDetails$ = new BehaviorSubject<UserDetails | null>(null);

    constructor(private _http: HttpClient) { }

    updateUserDetails(userDetailsForm: UserDetailsForm): Observable<void> {
        return of(this._userDetails$.value?.id).pipe(
            switchMap(id => {
                if (id) {
                    return this.updateCurrentUser(userDetailsForm);
                } else {
                    return this.createCurrentUser(userDetailsForm);
                }
            }),
            switchMap(userDetails => of(this._userDetails$.next(userDetails))),
        );
    }

    getCurrentUserDetails(): Observable<UserDetails> {
        if (!this._userDetails$.value) {
            return this._http.get<UserDetails>(`${appConfig.apiUrl}/user/user`).pipe(
                catchError((_error: HttpErrorResponse) => {
                    return of({
                        id: null,
                        personalId: '',
                        name: '',
                        surname: '',
                        phoneNumber: '',
                        email: '',
                        address: {
                            voivodeshipId: null,
                            city: '',
                            postalCode: '',
                            street: '',
                            buildingNumber: ''
                        },
                        roles: [],
                        sex: Gender.Male
                    } as UserDetails);
                }),
                tap(userDetails => this._userDetails$.next(userDetails))
            );
        }
        return this._userDetails$.asObservable().pipe(
            map(value => value as UserDetails)
        );
    }

    private updateCurrentUser(userDetails: UserDetailsForm): Observable<UserDetails> {
        return this._http.put<UserDetails>(`${appConfig.apiUrl}/user/user`, {
            ...userDetails
        });
    }

    private createCurrentUser(userDetails: UserDetailsForm): Observable<UserDetails> {
        return this._http.post<UserDetails>(`${appConfig.apiUrl}/user/user`, {
            ...userDetails
        });
    }
}
