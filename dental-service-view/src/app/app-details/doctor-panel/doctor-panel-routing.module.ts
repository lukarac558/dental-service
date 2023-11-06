import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DoctorInfoEditComponent } from './doctor-info-edit/doctor-info-edit.component';
import { DoctorPanelComponent } from './doctor-panel/doctor-panel.component';
import { DoctorServicesEditComponent } from './doctor-services-edit/doctor-services-edit.component';

const routes: Routes = [
    {
        path: '',
        component: DoctorPanelComponent
    },
    {
        path: 'edit-competency',
        component: DoctorInfoEditComponent
    },
    {
        path: 'edit-services',
        component: DoctorServicesEditComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DoctorPanelRoutingModule { }
