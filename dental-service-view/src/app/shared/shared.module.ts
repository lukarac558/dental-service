import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ErrorMessageModule } from './components/error-message/error-message.module';
import { SpinnerModule } from './components/spinner/spinner.module';

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        ErrorMessageModule,
        SpinnerModule,
    ],
    exports: [
        CommonModule,
        ErrorMessageModule,
        SpinnerModule,
    ]
})
export class SharedModule { }
