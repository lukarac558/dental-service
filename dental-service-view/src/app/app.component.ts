import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, map, Observable, shareReplay, tap } from 'rxjs';

import { AuthService } from './core/services/auth.service';

@Component({
    selector: 'ds-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    isMobile$ = this._observer.observe(['(max-width: 768px)']).pipe(
        map(res => res.matches),
        tap(isMobile => this._isMenuOpen$.next(!isMobile)),
        shareReplay({ refCount: true, bufferSize: 1 })
    );
    isAuth$: Observable<boolean>;

    private _isMenuOpen$ = new BehaviorSubject<boolean>(false);

    get isMenuOpen$(): Observable<boolean> {
        return this._isMenuOpen$.asObservable();
    }

    constructor(
        private _observer: BreakpointObserver,
        private _authService: AuthService
    ) { }

    ngOnInit(): void {
        this.isAuth$ = this._authService.isAuth$;
    }

    menuToggle(): void {
        this._isMenuOpen$.next(!this._isMenuOpen$.value);
    }

    onSidenavClose(): void {
        this._isMenuOpen$.next(false);
    }
}
