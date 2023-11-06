import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CalendarModule } from 'angular-calendar';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { MaterialModule } from 'src/app/material.module';
import { ErrorMessageModule } from 'src/app/shared/components/error-message/error-message.module';
import { ModalsModule } from 'src/app/shared/components/modals/modals.module';
import { SpinnerButtonModule } from 'src/app/shared/components/spinner-button/spinner-button.module';
import { SpinnerModule } from 'src/app/shared/components/spinner/spinner.module';
import { PipesModule } from 'src/app/shared/pipes/pipes.module';

import { DoctorInfoEditComponent } from './doctor-info-edit/doctor-info-edit.component';
import { DoctorPanelRoutingModule } from './doctor-panel-routing.module';
import { DoctorInfoComponent } from './doctor-panel/doctor-info/doctor-info.component';
import { DoctorPanelComponent } from './doctor-panel/doctor-panel.component';
import { AddWorkingDayModalComponent } from './doctor-panel/doctor-schedule/add-schedule-modal/add-schedule-modal.component';
import { DoctorScheduleComponent } from './doctor-panel/doctor-schedule/doctor-schedule.component';
import { DoctorServicesComponent } from './doctor-panel/doctor-services/doctor-services.component';
import { DoctorServiceModalComponent } from './doctor-services-edit/doctor-service-modal/doctor-service-modal.component';
import { DoctorServicesEditComponent } from './doctor-services-edit/doctor-services-edit.component';

@NgModule({
    declarations: [
        DoctorInfoComponent,
        DoctorInfoEditComponent,
        DoctorPanelComponent,
        DoctorScheduleComponent,
        AddWorkingDayModalComponent,
        DoctorServicesComponent,
        DoctorServicesEditComponent,
        DoctorServiceModalComponent
    ],
    imports: [
        CommonModule,
        DoctorPanelRoutingModule,
        MaterialModule,
        ReactiveFormsModule,
        ErrorMessageModule,
        SpinnerButtonModule,
        SpinnerModule,
        CalendarModule,
        NgxMaterialTimepickerModule,
        PipesModule,
        ModalsModule
    ]
})
export class DoctorPanelModule { }
