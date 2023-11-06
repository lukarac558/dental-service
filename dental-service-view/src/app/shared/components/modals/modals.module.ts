import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MaterialModule } from 'src/app/material.module';

import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';

@NgModule({
    declarations: [
        ConfirmationModalComponent
    ],
    exports: [
        ConfirmationModalComponent
    ],
    imports: [
        CommonModule,
        MaterialModule
    ]
})
export class ModalsModule { }
