import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { DoctorCompetency, DoctorCompetencyForm } from 'src/app/core/models/doctor.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-doctor-info-edit',
    templateUrl: './doctor-info-edit.component.html'
})
export class DoctorInfoEditComponent implements OnInit, OnDestroy {
    doctorCompetency$: Observable<DoctorCompetency>;
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
        this.form = this._fb.group<ControlsOf<DoctorCompetencyForm>>({
            id: this._fb.control(null),
            description: this._fb.nonNullable.control('', [Validators.required]),
            title: this._fb.nonNullable.control('', [Validators.required])
        });

        this.doctorCompetency$ = this._doctorsService.getCurrentDoctorCompetency().pipe(
            tap(doctorCompetency => this.form.patchValue(doctorCompetency))
        );
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const doctorCompetency = this.form.value as DoctorCompetencyForm;

            this._doctorsService.updateCurrentDoctorCompetency(doctorCompetency).pipe(
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
