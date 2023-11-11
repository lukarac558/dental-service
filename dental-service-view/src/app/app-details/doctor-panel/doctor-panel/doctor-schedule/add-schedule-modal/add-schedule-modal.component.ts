import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ToastrService } from 'ngx-toastr';
import { Subject, switchMap, takeUntil } from 'rxjs';
import { DoctorAddScheduleForm } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import { UsersService } from 'src/app/core/services/users.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { ControlsOf } from 'src/main';

export type AddWorkingDayModalData = {
    date: Date;
};

@Component({
    selector: 'ds-add-working-day-modal',
    templateUrl: './add-schedule-modal.component.html',
    styleUrls: ['./add-schedule-modal.component.scss']
})
export class AddWorkingDayModalComponent implements OnInit, OnDestroy {
    date: Moment;
    form: FormGroup;
    isSaving$ = new Subject<boolean>();

    private _destroy$ = new Subject<void>();

    constructor(
        private _dialogRef: MatDialogRef<AddWorkingDayModalComponent>,
        @Inject(MAT_DIALOG_DATA) private _data: AddWorkingDayModalData,
        private _fb: FormBuilder,
        private _toastr: ToastrService,
        private _doctorsService: DoctorsService,
        private _userService: UsersService
    ) {
        this.date = moment(this._data.date.toISOString());
    }

    ngOnInit(): void {
        this.form = this._fb.group<ControlsOf<DoctorAddScheduleForm>>({
            startDate: this._fb.control({ value: this.date, disabled: true }, [Validators.required]),
            startTime: this._fb.nonNullable.control('', [Validators.required]),
            workDuration: this._fb.nonNullable.control('', [Validators.required])
        });
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const doctorAddSchedule = this.form.getRawValue() as DoctorAddScheduleForm;
            const time = doctorAddSchedule.startTime.split(':');
            doctorAddSchedule.startDate?.hour(+time[0]).minute(+time[1]);

            this._userService.getCurrentUserDetails().pipe(
                switchMap(user => this._doctorsService.addCurrentDoctorSchedule({
                    doctorId: user.id as number,
                    startDate: doctorAddSchedule.startDate?.format("YYYY-MM-DD HH:mm:ss") as string,
                    workDuration: doctorAddSchedule.workDuration + ":00"
                })),
                takeUntil(this._destroy$),
                indicate(this.isSaving$)
            ).subscribe(_ => {
                this._toastr.success('Pomyślnie dodano dzień roboczy.');
                this._dialogRef.close(true);
            });
        } else {
            this.form.markAllAsTouched();
        }
    }
}
