import { Component, Input } from '@angular/core';
import { DoctorShort } from 'src/app/core/models/doctor.model';
import { Role } from 'src/app/core/models/role.model';

@Component({
    selector: 'ds-doctor-brief',
    templateUrl: './doctor-brief.component.html',
    styleUrls: ['./doctor-brief.component.scss']
})
export class DoctorComponent {
    @Input() doctor: DoctorShort;

    role = Role;
}
