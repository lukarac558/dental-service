import { Component, OnInit } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { DoctorCompetency, DoctorService } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';

@Component({
    selector: 'ds-doctor-panel',
    templateUrl: './doctor-panel.component.html'
})
export class DoctorPanelComponent implements OnInit {
    details$: Observable<{
        doctorCompetency: DoctorCompetency,
        doctorServices: DoctorService[]
    }>;

    constructor(
        private _doctorsService: DoctorsService
    ) { }

    ngOnInit(): void {
        this.details$ = forkJoin({
            doctorCompetency: this._doctorsService.getCurrentDoctorCompetency(),
            doctorServices: this._doctorsService.getCurrentDoctorServices()
        });
    }
}
