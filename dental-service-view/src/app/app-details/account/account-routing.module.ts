import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CanDeactivateAsUnregisteredGuard } from 'src/app/core/guards/can-deactivate-as-unregistered.guard';

import { AccountEditComponent } from './account-edit/account-edit.component';
import { AccountComponent } from './account/account.component';

const routes: Routes = [
    {
        path: '',
        component: AccountComponent
    },
    {
        path: 'edit',
        component: AccountEditComponent,
        canDeactivate: [CanDeactivateAsUnregisteredGuard]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AccountRoutingModule { }
