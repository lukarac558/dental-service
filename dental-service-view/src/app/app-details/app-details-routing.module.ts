import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from '../core/guards/auth.guard';

const routes: Routes = [
    {
        path: '',
        children: [
            {
                path: "account",
                loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
                canActivate: [AuthGuard]
            },
            {
                path: "doctors",
                loadChildren: () => import('./doctors/doctors.module').then(m => m.DoctorsModule),
                canActivate: [AuthGuard]
            },
            {
                path: "visits",
                loadChildren: () => import('./visits/visits.module').then(m => m.VisitsModule),
                canActivate: [AuthGuard]
            },
            {
                path: "doctor-panel",
                loadChildren: () => import('./doctor-panel/doctor-panel.module').then(m => m.DoctorPanelModule),
                canActivate: [AuthGuard]
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AppDetailsRoutingModule { }
