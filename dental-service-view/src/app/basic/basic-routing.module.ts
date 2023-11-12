import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomePageComponent } from './home-page/home-page.component';
import { NotAuthGuard } from '../core/guards/not-auth.guard';

const routes: Routes = [
    {
        path: '',
        component: HomePageComponent,
        pathMatch: 'full',
        canActivate: [NotAuthGuard]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MainRoutingModule { }
