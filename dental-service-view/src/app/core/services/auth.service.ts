import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, from, Observable } from 'rxjs';
import { authConfig } from 'src/app/auth.config';
import { indicate } from 'src/app/shared/operators/indicate';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuth$ = new BehaviorSubject<boolean>(false);
    private _isLoading$ = new BehaviorSubject<boolean>(false);

    constructor(
        private _oauthService: OAuthService
    ) {
        this._oauthService.configure(authConfig);
        this._oauthService.setupAutomaticSilentRefresh();

        from(this._oauthService.loadDiscoveryDocumentAndTryLogin()).pipe(
            indicate(this._isLoading$)
        ).subscribe(_ => {
            this._isAuth$.next(this._oauthService.hasValidAccessToken());
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
