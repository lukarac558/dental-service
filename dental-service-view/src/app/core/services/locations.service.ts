import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { appConfig } from 'src/app/app.config';

import { Lookup } from '../models/lookup.model';

@Injectable({
    providedIn: 'root'
})
export class LocationsService {
    private _voivodeships$: Observable<Lookup[]>;

    constructor(private _http: HttpClient) { }

    getVoivodeships(): Observable<Lookup[]> {
        if (!this._voivodeships$) {
            this._voivodeships$ = this._http.get<Lookup[]>(`${appConfig.apiUrl}/location/voivodeships`).pipe(
                shareReplay({ refCount: true, bufferSize: 1 })
            );
        }
        return this._voivodeships$;
    }
}
