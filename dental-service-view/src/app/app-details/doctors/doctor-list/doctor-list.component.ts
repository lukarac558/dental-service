import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import { DoctorSearch, DoctorShort } from 'src/app/core/models/doctor.model';
import { CustomPageCriteria, Page } from 'src/app/core/models/page.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';

@Component({
    selector: 'ds-doctor-list',
    templateUrl: './doctor-list.component.html'
})
export class DoctorListComponent implements OnInit {
    doctors$: Observable<Page<DoctorShort>>;
    searchCriteria$ = new BehaviorSubject<CustomPageCriteria<DoctorSearch>>({
        page: 0,
        pageSize: 6,
        name: '',
        service: ''
    });

    constructor(
        private _doctorsService: DoctorsService
    ) { }

    ngOnInit(): void {
        this.doctors$ = this.searchCriteria$.pipe(
            switchMap(criteria => this._doctorsService.getDoctors(criteria))
        );
    }

    onPageChange(value: PageEvent): void {
        this.searchCriteria$.next({
            ...this.searchCriteria$.value,
            page: value.pageIndex,
            pageSize: value.pageSize
        });
    }

    onSearch(search: DoctorSearch): void {
        this.searchCriteria$.next({
            pageSize: this.searchCriteria$.value.pageSize,
            page: 0,
            ...search
        });
    }
}
