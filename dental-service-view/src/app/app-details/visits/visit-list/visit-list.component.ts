import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import { Page, PageCriteria } from 'src/app/core/models/page.model';
import { Visit } from 'src/app/core/models/visits.model';
import { UsersService } from 'src/app/core/services/users.service';
import { VisitsService } from 'src/app/core/services/visits.service';

@Component({
    selector: 'ds-visit-list',
    templateUrl: './visit-list.component.html'
})
export class VisitListComponent implements OnInit {
    visits$: Observable<Page<Visit>>;
    reservedVisits$: Observable<Visit[]>;
    searchCriteria$ = new BehaviorSubject<PageCriteria>({
        page: 0,
        pageSize: 6
    });

    private _refreshReservedVisits$ = new BehaviorSubject<void>(undefined);

    constructor(
        private _visitsService: VisitsService,
        private _toastr: ToastrService,
        private _userService: UsersService
    ) { }

    ngOnInit(): void {
        this.visits$ = this.searchCriteria$.pipe(
            switchMap(_ => this._userService.getCurrentUserDetails()),
            switchMap(result => this._visitsService.getVisits(result.id as number, this.searchCriteria$.value))
        );

        this.reservedVisits$ = this._refreshReservedVisits$.pipe(
            switchMap(_ => this._userService.getCurrentUserDetails()),
            switchMap(user => this._visitsService.getReservedVisits(user.id as number))
        );
    }

    onPageChange(value: PageEvent): void {
        this.searchCriteria$.next({
            ...this.searchCriteria$.value,
            page: value.pageIndex,
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
