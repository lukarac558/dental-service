import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { HasRolePipe } from './has-role.pipe';
import { StringToDatePipe } from './string-to-date.pipe';
import { TimespanPipe } from './timespan.pipe';
import { VoivodeshipPipe } from './voivodeship.pipe';

@NgModule({
    declarations: [
        HasRolePipe,
        VoivodeshipPipe,
        TimespanPipe,
        StringToDatePipe
    ],
    exports: [
        HasRolePipe,
        VoivodeshipPipe,
        TimespanPipe,
        StringToDatePipe
    ],
    imports: [
        CommonModule
    ]
})
export class PipesModule { }
