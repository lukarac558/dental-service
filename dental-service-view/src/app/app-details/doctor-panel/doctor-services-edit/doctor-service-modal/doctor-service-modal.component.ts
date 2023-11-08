import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { of, Subject, switchMap, takeUntil } from 'rxjs';
import { DoctorService, DoctorServiceForm } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { ControlsOf } from 'src/main';

export type DoctorServiceModal = {
    service?: DoctorService | null;
};

@Component({
    selector: 'ds-doctor-service-modal',
    templateUrl: './doctor-service-modal.component.html',
    styleUrls: ['./doctor-service-modal.component.scss']
})
export class DoctorServiceModalComponent implements OnInit, OnDestroy {
    form: FormGroup;
    isSaving$ = new Subject<boolean>();
    defaultDurationTime = '01:00'

    private _destroy$ = new Subject<void>();
    private _service: DoctorService | null = null;

    constructor(
        private _dialogRef: MatDialogRef<DoctorServiceModalComponent>,
        @Inject(MAT_DIALOG_DATA) private _data: DoctorServiceModal,
        private _fb: FormBuilder,
        private _toastr: ToastrService,
        private _doctorsService: DoctorsService
    ) {
        if (!!this._data.service) {
            this._service = this._data.service;
            this.defaultDurationTime = this._service.durationTime;
        }
    }

    ngOnInit(): void {
        const durationTime = this._service?.durationTime ? this._service?.durationTime.substring(0, 5) : this.defaultDurationTime;
        this.form = this._fb.group<ControlsOf<DoctorServiceForm>>({
            name: this._fb.nonNullable.control(this._service?.name || '', [Validators.required, Validators.maxLength(100)]),
            durationTime: this._fb.nonNullable.control(durationTime, [Validators.required]),
            description: this._fb.nonNullable.control(this._service?.description || '', [Validators.required, Validators.maxLength(200)])
        });
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const service = {
                ...this.form.value,
                durationTime: this.form.value.durationTime + ":00"
            } as DoctorServiceForm;
            of(this._service?.id).pipe(
                switchMap(id => {
                    if (!!id) {
                        return this._doctorsService.updateCurrentDoctorService(id, service);
                    } else {
                        return this._doctorsService.addCurrentDoctorService(service);
                    }
                }),
                takeUntil(this._destroy$),
                indicate(this.isSaving$)
            ).subscribe(_ => {
                this._toastr.success('Pomyślnie zapisano usługę');
                this._dialogRef.close(true);
            })
        } else {
            this.form.markAllAsTouched();
        }
    }
}
