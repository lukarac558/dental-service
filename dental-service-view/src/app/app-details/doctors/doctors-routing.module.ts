import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DoctorListComponent } from './doctor-list/doctor-list.component';
import { DoctorComponent } from './doctor/doctor.component';

const routes: Routes = [
    {
        path: '',
        component: DoctorListComponent
    },
    {
        path: ':id',
        component: DoctorComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DoctorsRoutingModule { }
