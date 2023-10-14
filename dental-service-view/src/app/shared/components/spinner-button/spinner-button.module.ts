import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MaterialModule } from 'src/app/material.module';

import { SpinnerButtonComponent } from './spinner-button.component';

@NgModule({
    declarations: [
        SpinnerButtonComponent
    ],
    imports: [
        CommonModule,
        MaterialModule
    ],
    exports: [
        SpinnerButtonComponent
    ]
})
export class SpinnerButtonModule { }
