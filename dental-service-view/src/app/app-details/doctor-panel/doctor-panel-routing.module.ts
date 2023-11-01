import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DoctorInfoEditComponent } from './doctor-info-edit/doctor-info-edit.component';
import { DoctorPanelComponent } from './doctor-panel/doctor-panel.component';

const routes: Routes = [
    {
        path: '',
        component: DoctorPanelComponent
    },
    {
        path: 'edit',
        component: DoctorInfoEditComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DoctorPanelRoutingModule { }
