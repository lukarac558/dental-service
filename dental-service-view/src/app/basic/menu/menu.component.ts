import { Component } from '@angular/core';
import { appConfig } from 'src/app/app.config';
import { Role } from 'src/app/core/models/role.model';

@Component({
    selector: 'ds-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.scss']
})
export class MenuComponent {
    role = Role;

    redirectToAuthSecurity(): void {
        window.location.href = appConfig.authSecurity;
    }
}
