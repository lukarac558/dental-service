import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, from, map, Observable, of, switchMap, tap } from 'rxjs';
import { authConfig } from 'src/app/app.config';

import { UsersService } from './users.service';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuth$ = new BehaviorSubject<boolean>(false);
    private _isLoading$ = new BehaviorSubject<boolean>(false);

    constructor(
        private _oauthService: OAuthService,
        private _usersService: UsersService,
        private _router: Router
    ) {
        this._oauthService.configure(authConfig);
        this._oauthService.setupAutomaticSilentRefresh();

        this._isLoading$.next(true);
        from(this._oauthService.loadDiscoveryDocumentAndTryLogin()).pipe(
            tap(_ => this._isAuth$.next(this._oauthService.hasValidAccessToken())),
            switchMap(_ => {
                if (this._isAuth$.value) {
                    return this._usersService.getCurrentUserDetails().pipe(
                        map(result => !!result.id)
                    );
                } else {
                    return of(true);
                }
            })
        ).subscribe(result => {
            if (!result) {
                this._router.navigateByUrl('/account/edit');
            }
            this._isLoading$.next(false);
        });
    }

    get isAuth$(): Observable<boolean> {
        return this._isAuth$.asObservable();
    }

    get isLoading$(): Observable<boolean> {
        return this._isLoading$.asObservable();
    }

    login(): void {
        this._oauthService.initCodeFlow();
    }

    logout(): void {
        this._oauthService.logOut();
    }
}
