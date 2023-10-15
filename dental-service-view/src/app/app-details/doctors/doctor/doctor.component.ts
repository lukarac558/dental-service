import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Doctor } from 'src/app/core/models/doctor.model';
import { Role } from 'src/app/core/models/role.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';

@Component({
    selector: 'ds-doctor',
    templateUrl: './doctor.component.html',
    styleUrls: ['./doctor.component.scss']
})
export class DoctorComponent implements OnInit {
    doctorId: number;
    doctor$: Observable<Doctor>;
    role = Role;

    constructor(
        private _doctorsService: DoctorsService,
        private _activatedRoute: ActivatedRoute
    ) {
        this.doctorId = +this._activatedRoute.snapshot.params.id;
    }

    ngOnInit(): void {
        this.doctor$ = this._doctorsService.getDoctor(this.doctorId);
    }
}
