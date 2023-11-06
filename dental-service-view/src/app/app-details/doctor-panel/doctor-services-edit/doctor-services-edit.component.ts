import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, filter, Observable, switchMap } from 'rxjs';
import { DoctorService } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import {
    ConfirmationModalComponent
} from 'src/app/shared/components/modals/confirmation-modal/confirmation-modal.component';

import { DoctorServiceModalComponent } from './doctor-service-modal/doctor-service-modal.component';

@Component({
    selector: 'ds-doctor-services-edit',
    templateUrl: './doctor-services-edit.component.html',
    styleUrls: ['./doctor-services-edit.component.scss']
})
export class DoctorServicesEditComponent implements OnInit {
    doctorServices$: Observable<DoctorService[]>;

    private _refreshServices$ = new BehaviorSubject<void>(undefined);

    constructor(
        private _doctorsService: DoctorsService,
        private _toastr: ToastrService,
        private _dialog: MatDialog
    ) { }

    ngOnInit(): void {
        this.doctorServices$ = this._refreshServices$.pipe(
            switchMap(_ => this._doctorsService.getCurrentDoctorServices())
        );
    }

    openDoctorServiceModal(service: DoctorService | null): void {
        this._dialog.open(DoctorServiceModalComponent, {
            width: '450px',
            data: {
                service,
            },
        }).afterClosed().pipe(
            filter(result => result)
        ).subscribe(_ => {
            this._refreshServices$.next();
        });
    }

    deleteService(service: DoctorService): void {
        this._dialog.open(ConfirmationModalComponent, {
            width: '450px',
            data: {
                title: 'Usuwanie usługi',
                description: 'Czy na pewno chcesz usunąć usługę?'
            },
        }).afterClosed().pipe(
            filter(result => !!result),
            switchMap(_ => this._doctorsService.deleteCurrentDoctorService(service.id))
        ).subscribe(_ => {
            this._refreshServices$.next();
            this._toastr.success("Pomyślnie usunięto usługę")
        });
    }
}
