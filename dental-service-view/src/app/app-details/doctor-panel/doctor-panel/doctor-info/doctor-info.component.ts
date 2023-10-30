import { Component, Input } from '@angular/core';
import { DoctorInfo } from 'src/app/core/models/doctor.model';

@Component({
    selector: 'ds-doctor-info',
    templateUrl: './doctor-info.component.html',
    styleUrls: ['./doctor-info.component.scss']
})
export class DoctorInfoComponent {
    @Input() doctorInfo: DoctorInfo;
}
