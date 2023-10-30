import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { DoctorInfo } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';

@Component({
    selector: 'ds-doctor-panel',
    templateUrl: './doctor-panel.component.html'
})
export class DoctorPanelComponent implements OnInit {
    doctorInfo$: Observable<DoctorInfo>;

    constructor(
        private _doctorsService: DoctorsService
    ) { }

    ngOnInit(): void {
        this.doctorInfo$ = this._doctorsService.getCurrentDoctorInfo();
    }
}
