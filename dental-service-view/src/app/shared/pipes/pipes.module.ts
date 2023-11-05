import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { HasRolePipe } from './has-role.pipe';
import { VoivodeshipPipe } from './voivodeship.pipe';

@NgModule({
    declarations: [
        HasRolePipe,
        VoivodeshipPipe
    ],
    exports: [
        HasRolePipe,
        VoivodeshipPipe
    ],
    imports: [
        CommonModule
    ]
})
export class PipesModule { }
