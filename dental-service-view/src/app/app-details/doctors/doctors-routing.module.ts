import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AddVisitComponent } from './add-visit/add-visit.component';
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
    },
    {
        path: ':id/add-visit',
        component: AddVisitComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DoctorsRoutingModule { }
