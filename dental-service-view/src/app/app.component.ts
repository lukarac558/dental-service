import { BreakpointObserver } from '@angular/cdk/layout';
import { Component } from '@angular/core';
import { BehaviorSubject, map, Observable, shareReplay, tap } from 'rxjs';

@Component({
    selector: 'ds-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    isMobile$ = this._observer.observe(['(max-width: 768px)']).pipe(
        map(res => res.matches),
        tap(isMobile => this._isMenuOpen$.next(!isMobile)),
        shareReplay({ refCount: true, bufferSize: 1 })
    );

    private _isMenuOpen$ = new BehaviorSubject<boolean>(false);

    get isMenuOpen$(): Observable<boolean> {
        return this._isMenuOpen$.asObservable();
    }

    constructor(private _observer: BreakpointObserver) { }

    menuToggle(): void {
        this._isMenuOpen$.next(!this._isMenuOpen$.value);
    }
}
