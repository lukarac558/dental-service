import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import { Page, PageCriteria } from 'src/app/core/models/page.model';
import { ReservationVisit, Visit } from 'src/app/core/models/visits.model';
import { VisitsService } from 'src/app/core/services/visits.service';

@Component({
    selector: 'ds-visit-list',
    templateUrl: './visit-list.component.html'
})
export class VisitListComponent implements OnInit {
    visits$: Observable<Page<Visit>>;
    reservedVisits$: Observable<ReservationVisit[]>;
    searchCriteria$ = new BehaviorSubject<PageCriteria>({
        pageIndex: 0,
        pageSize: 6
    });

    private _refreshReservedVisits$ = new BehaviorSubject<void>(undefined);

    constructor(
        private _visitsService: VisitsService,
        private _toastr: ToastrService
    ) { }

    ngOnInit(): void {
        this.visits$ = this.searchCriteria$.pipe(
            switchMap(searchCriteria => this._visitsService.getVisits(searchCriteria))
        );

        this.reservedVisits$ = this._refreshReservedVisits$.pipe(
            switchMap(_ => this._visitsService.getReservedVisits())
        );
    }

    onPageChange(value: PageEvent): void {
        this.searchCriteria$.next({
            ...this.searchCriteria$.value,
            pageIndex: value.pageIndex,
            pageSize: value.pageSize
        });
    }

    onVisitConfirm(id: number): void {
        this._visitsService.confirmVisit(id).subscribe(_ => {
            this._toastr.success("Pomyślnie potwierdzono wizytę");
            this._refreshReservedVisits$.next();
            this.searchCriteria$.next(this.searchCriteria$.value);
        });
    }
}
