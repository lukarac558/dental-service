import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material.module';
import { AvatarModule } from 'src/app/shared/components/avatar/avatar.module';
import { ErrorMessageModule } from 'src/app/shared/components/error-message/error-message.module';
import { SpinnerButtonModule } from 'src/app/shared/components/spinner-button/spinner-button.module';
import { SpinnerModule } from 'src/app/shared/components/spinner/spinner.module';

import { AddVisitComponent } from './add-visit/add-visit.component';
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
        DoctorComponent,
        AddVisitComponent
    ],
    imports: [
        CommonModule,
        DoctorsRoutingModule,
        MaterialModule,
        SpinnerModule,
        AvatarModule,
        ReactiveFormsModule,
        ErrorMessageModule,
        SpinnerButtonModule
    ]
})
export class DoctorsModule { }
