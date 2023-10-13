import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AccountEditComponent } from './account-edit/account-edit.component';
import { AccountComponent } from './account/account.component';

const routes: Routes = [
    {
        path: '',
        component: AccountComponent
    },
    {
        path: 'edit',
        component: AccountEditComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AccountRoutingModule { }
