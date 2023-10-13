import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuth$ = new BehaviorSubject<boolean>(false);

    constructor() { }

    get isAuth$(): Observable<boolean> {
        return this._isAuth$.asObservable();
    }

    login(): void {
        this._isAuth$.next(true);
    }

    logout(): void {
        this._isAuth$.next(false);
    }
}
