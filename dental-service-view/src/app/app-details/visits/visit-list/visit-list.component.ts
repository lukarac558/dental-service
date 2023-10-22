import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import { Page, PageCriteria } from 'src/app/core/models/page.model';
import { Visit } from 'src/app/core/models/visits.model';
import { VisitsService } from 'src/app/core/services/visits.service';

@Component({
    selector: 'ds-visit-list',
    templateUrl: './visit-list.component.html'
})
export class VisitListComponent implements OnInit {
    visits$: Observable<Page<Visit>>;
    searchCriteria$ = new BehaviorSubject<PageCriteria>({
        pageIndex: 0,
        pageSize: 6
    });

    constructor(
        private _visitsService: VisitsService
    ) { }

    ngOnInit(): void {
        this.visits$ = this.searchCriteria$.pipe(
            switchMap(searchCriteria => this._visitsService.getVisits(searchCriteria))
        );
    }

    onPageChange(value: PageEvent): void {
        this.searchCriteria$.next({
            ...this.searchCriteria$.value,
            pageIndex: value.pageIndex,
            pageSize: value.pageSize
        });
    }
}
