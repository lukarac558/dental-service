import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, delay, EMPTY, Observable, startWith } from 'rxjs';
import { indicate } from 'src/app/shared/operators/indicate';

import { LoginForm, RegisterForm } from '../models/login-register.model';
import { InitUserDetailsForm } from '../models/applicationUser.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuth$ = new BehaviorSubject<boolean>(true);
    private _isLoading$ = new BehaviorSubject<boolean>(false);

    constructor(private _router: Router) { }

    get isAuth$(): Observable<boolean> {
        return this._isAuth$.asObservable();
    }

    get isLoading$(): Observable<boolean> {
        return this._isLoading$.asObservable();
    }

    login(credentials: LoginForm): void {
        EMPTY.pipe(
            startWith(undefined),
            delay(1000),
            indicate(this._isLoading$)
        ).subscribe(_ => {
            this._isAuth$.next(true);
            this._router.navigateByUrl('/');
        });
    }

    register(credentials: RegisterForm, userDetails: InitUserDetailsForm): void {
        EMPTY.pipe(
            startWith(undefined),
            delay(1000),
            indicate(this._isLoading$)
        ).subscribe(_ => {
            this._isAuth$.next(true);
            this._router.navigateByUrl('/');
        });
    }

    logout(): void {
        EMPTY.pipe(
            startWith(undefined),
            delay(1000),
            indicate(this._isLoading$)
        ).subscribe(_ => {
            this._isAuth$.next(false);
        });
    }
}
