import { Component } from '@angular/core';
import { Role } from 'src/app/core/models/role.model';

@Component({
    selector: 'ds-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.scss']
})
export class MenuComponent {
    role = Role;
}
