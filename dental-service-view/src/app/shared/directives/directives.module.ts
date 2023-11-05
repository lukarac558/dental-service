import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { RequiredRolesDirective } from './required-roles.directive';

@NgModule({
    declarations: [
        RequiredRolesDirective
    ],
    exports: [
        RequiredRolesDirective
    ],
    imports: [
        CommonModule
    ]
})
export class DirectivesModule { }
