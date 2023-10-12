import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MaterialModule } from 'src/app/material.module';

import { SpinnerComponent } from './spinner.component';

@NgModule({
    declarations: [
        SpinnerComponent
    ],
    exports: [
        SpinnerComponent
    ],
    imports: [
        CommonModule,
        MaterialModule
    ]
})
export class SpinnerModule { }
