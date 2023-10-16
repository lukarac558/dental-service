import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';
import { AvatarModule } from 'src/app/shared/components/avatar/avatar.module';
import { SpinnerModule } from 'src/app/shared/components/spinner/spinner.module';

import { DoctorBriefComponent } from './doctor-list/doctor-brief/doctor-brief.component';
import { DoctorListComponent } from './doctor-list/doctor-list.component';
import { DoctorSearchComponent } from './doctor-list/doctor-search/doctor-search.component';
import { DoctorComponent } from './doctor/doctor.component';
import { DoctorsRoutingModule } from './doctors-routing.module';

@NgModule({
    declarations: [
        DoctorListComponent,
        DoctorBriefComponent,
        DoctorSearchComponent,
        DoctorComponent
    ],
    imports: [
        CommonModule,
        DoctorsRoutingModule,
        MaterialModule,
        SpinnerModule,
        AvatarModule,
        ReactiveFormsModule
    ]
})
export class DoctorsModule { }
