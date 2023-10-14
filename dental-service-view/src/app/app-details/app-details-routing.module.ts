import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    {
        path: '',
        children: [
            {
                path: "login",
                loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
                //canActivate: [NotAuthGuard]
            },
            {
                path: "register",
                loadChildren: () => import('./register/register.module').then(m => m.RegisterModule),
                //canActivate: [NotAuthGuard]
            },
            {
                path: "account",
                loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
                //canActivate: [AuthGuard]
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AppDetailsRoutingModule { }
