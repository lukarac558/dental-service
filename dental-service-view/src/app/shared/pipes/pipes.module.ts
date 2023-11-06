import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { HasRolePipe } from './has-role.pipe';
import { TimespanPipe } from './timespan.pipe';
import { VoivodeshipPipe } from './voivodeship.pipe';

@NgModule({
    declarations: [
        HasRolePipe,
        VoivodeshipPipe,
        TimespanPipe
    ],
    exports: [
        HasRolePipe,
        VoivodeshipPipe,
        TimespanPipe
    ],
    imports: [
        CommonModule
    ]
})
export class PipesModule { }
