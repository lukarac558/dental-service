import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MaterialModule } from 'src/app/material.module';
import { SpinnerModule } from 'src/app/shared/components/spinner/spinner.module';

import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account/account.component';

@NgModule({
    declarations: [
        AccountComponent
    ],
    imports: [
        CommonModule,
        AccountRoutingModule,
        MaterialModule,
        SpinnerModule
    ]
})
export class AccountModule { }
