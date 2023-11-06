import { Component, Input } from '@angular/core';
import { DoctorService } from 'src/app/core/models/doctor.model';

@Component({
    selector: 'ds-doctor-services',
    templateUrl: './doctor-services.component.html',
    styleUrls: ['./doctor-services.component.scss']
})
export class DoctorServicesComponent {
    @Input() doctorServices: DoctorService[];
}
