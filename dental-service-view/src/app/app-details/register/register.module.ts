import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';
import { ErrorMessageModule } from 'src/app/shared/components/error-message/error-message.module';
import { SpinnerButtonModule } from 'src/app/shared/components/spinner-button/spinner-button.module';
import { SpinnerModule } from 'src/app/shared/components/spinner/spinner.module';

import { RegisterComponent } from '../register/register.component';
import { RegisterRoutingModule } from './register-routing.module';

@NgModule({
    declarations: [
        RegisterComponent
    ],
    imports: [
        CommonModule,
        RegisterRoutingModule,
        MaterialModule,
        SpinnerModule,
        SpinnerButtonModule,
        ErrorMessageModule,
        ReactiveFormsModule
    ]
})
export class RegisterModule { }
