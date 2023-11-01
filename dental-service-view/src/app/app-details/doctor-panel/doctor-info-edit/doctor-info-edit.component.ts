import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { DoctorInfo, DoctorInfoForm } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-doctor-info-edit',
    templateUrl: './doctor-info-edit.component.html'
})
export class DoctorInfoEditComponent implements OnInit, OnDestroy {
    doctorInfo$: Observable<DoctorInfo>;
    form: FormGroup;
    isSaving$ = new Subject<boolean>();

    private _destroy$ = new Subject<void>();

    constructor(
        private _doctorsService: DoctorsService,
        private _fb: FormBuilder,
        private _toastr: ToastrService,
        private _router: Router
    ) { }

    ngOnInit(): void {
        this.form = this._fb.group<ControlsOf<DoctorInfoForm>>({
            aboutMe: this._fb.nonNullable.control('', [Validators.required]),
            specialization: this._fb.nonNullable.control('', [Validators.required])
        });

        this.doctorInfo$ = this._doctorsService.getCurrentDoctorInfo().pipe(
            tap(doctorInfo => this.form.patchValue(doctorInfo))
        );
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const doctorInfo = this.form.value as DoctorInfoForm;

            this._doctorsService.updateCurrentDoctorInfo(doctorInfo).pipe(
                takeUntil(this._destroy$),
                indicate(this.isSaving$)
            ).subscribe(_ => {
                this._toastr.success('Pomyślnie zapisano informacje lekarskie.');
                this._router.navigateByUrl('/doctor-panel');
            });
        } else {
            this.form.markAllAsTouched();
        }
    }
}
