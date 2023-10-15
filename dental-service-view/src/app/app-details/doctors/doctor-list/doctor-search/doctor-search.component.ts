import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { DoctorSearch } from 'src/app/core/models/doctor.model';
import { Lookup } from 'src/app/core/models/lookup.model';
import { DoctorServicesService } from 'src/app/core/services/doctor-services.service';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-doctor-search',
    templateUrl: './doctor-search.component.html',
    styleUrls: ['./doctor-search.component.scss']
})
export class DoctorSearchComponent implements OnInit {
    @Output() searched = new EventEmitter<DoctorSearch>();

    searchForm: FormGroup;
    services$: Observable<Lookup[]>;

    constructor(
        private _fb: FormBuilder,
        private _doctorServices: DoctorServicesService
    ) { }

    ngOnInit(): void {
        this.searchForm = this._fb.group<ControlsOf<DoctorSearch>>({
            name: this._fb.nonNullable.control(''),
            serviceId: this._fb.control(null)
        });

        this.services$ = this._doctorServices.getDoctorServices();
    }

    search(): void {
        const search = this.searchForm.value as DoctorSearch;
        this.searched.emit(search);
    }

    clear(): void {
        this.searchForm.reset();
        this.search();
    }
}
