import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DoctorSearch } from 'src/app/core/models/doctor.model';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-doctor-search',
    templateUrl: './doctor-search.component.html',
    styleUrls: ['./doctor-search.component.scss']
})
export class DoctorSearchComponent implements OnInit {
    @Output() searched = new EventEmitter<DoctorSearch>();

    searchForm: FormGroup;

    constructor(
        private _fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.searchForm = this._fb.group<ControlsOf<DoctorSearch>>({
            name: this._fb.nonNullable.control(''),
            service: this._fb.nonNullable.control('')
        });
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
