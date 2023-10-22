import { Component, Input } from '@angular/core';
import { Role } from 'src/app/core/models/role.model';
import { Visit } from 'src/app/core/models/visits.model';

@Component({
    selector: 'ds-visit-item',
    templateUrl: './visit-item.component.html',
    styleUrls: ['./visit-item.component.scss']
})
export class VisitItemComponent {
    @Input() visit: Visit;
    now = new Date();
    role = Role;
    displayServices = false;
}
