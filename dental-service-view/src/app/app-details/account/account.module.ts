import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';
import { AvatarModule } from 'src/app/shared/components/avatar/avatar.module';
import { ErrorMessageModule } from 'src/app/shared/components/error-message/error-message.module';
import { SpinnerButtonModule } from 'src/app/shared/components/spinner-button/spinner-button.module';
import { SpinnerModule } from 'src/app/shared/components/spinner/spinner.module';
import { PipesModule } from 'src/app/shared/pipes/pipes.module';

import { AccountEditComponent } from './account-edit/account-edit.component';
import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account/account.component';

@NgModule({
    declarations: [
        AccountComponent,
        AccountEditComponent
    ],
    imports: [
        CommonModule,
        AccountRoutingModule,
        MaterialModule,
        SpinnerModule,
        ReactiveFormsModule,
        ErrorMessageModule,
        SpinnerButtonModule,
        AvatarModule,
        PipesModule
    ]
})
export class AccountModule { }
