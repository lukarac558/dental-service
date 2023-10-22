import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { VisitListComponent } from './visit-list/visit-list.component';

const routes: Routes = [
    {
        path: '',
        component: VisitListComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class VisitsRoutingModule { }
