import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from '../core/guards/auth.guard';
import { HasRolesGuard } from '../core/guards/has-roles.guard';
import { Role } from '../core/models/role.model';

const routes: Routes = [
    {
        path: '',
        canActivate: [AuthGuard],
        children: [
            {
                path: "account",
                loadChildren: () => import('./account/account.module').then(m => m.AccountModule)
            },
            {
                path: "doctors",
                loadChildren: () => import('./doctors/doctors.module').then(m => m.DoctorsModule)
            },
            {
                path: "visits",
                loadChildren: () => import('./visits/visits.module').then(m => m.VisitsModule)
            },
            {
                path: "doctor-panel",
                loadChildren: () => import('./doctor-panel/doctor-panel.module').then(m => m.DoctorPanelModule),
                canActivate: [HasRolesGuard],
                data: [Role.Doctor]
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AppDetailsRoutingModule { }
