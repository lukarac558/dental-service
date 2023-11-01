import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ToastrService } from 'ngx-toastr';
import { BehaviorSubject, Observable, Subject, filter, switchMap, takeUntil, tap } from 'rxjs';
import { Doctor } from 'src/app/core/models/doctor.model';
import { Role } from 'src/app/core/models/role.model';
import { VisitAvailableDate, VisitForm } from 'src/app/core/models/visits.model';
import { DoctorsService } from 'src/app/core/services/doctors.service';
import { VisitsService } from 'src/app/core/services/visits.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-add-visit',
    templateUrl: './add-visit.component.html',
    styleUrls: ['./add-visit.component.scss']
})
export class AddVisitComponent implements OnInit, OnDestroy {
    doctorId: number;
    doctor$: Observable<Doctor>;
    availableDates: VisitAvailableDate[];
    availableDays: Date[] = [];
    availableHours$ = new BehaviorSubject<string[]>([]);
    role = Role;
    form: FormGroup;
    isSaving$ = new Subject<boolean>();
    minDate = moment().add(1, 'day');

    private _destroy$ = new Subject<void>();

    constructor(
        private _doctorsService: DoctorsService,
        private _activatedRoute: ActivatedRoute,
        private _fb: FormBuilder,
        private _toastr: ToastrService,
        private _router: Router,
        private _visitsService: VisitsService
    ) {
        this.doctorId = +this._activatedRoute.snapshot.params.id;
    }

    ngOnInit(): void {
        this.form = this._fb.group<ControlsOf<VisitForm>>({
            doctorId: this._fb.nonNullable.control(this.doctorId, [Validators.required]),
            serviceIds: this._fb.control<number[]>([], [Validators.required]),
            date: this._fb.control({ value: null, disabled: true }, [Validators.required]),
            startHour: this._fb.nonNullable.control({ value: '', disabled: true }, [Validators.required])
        });

        this.doctor$ = this._doctorsService.getDoctor(this.doctorId);

        this.form.controls.serviceIds.valueChanges.pipe(
            takeUntil(this._destroy$),
            tap(_ => {
                this.form.controls.date.disable();
                this.form.controls.date.setValue(null);
            }),
            filter(serviceIds => serviceIds && serviceIds.length > 0),
            switchMap(serviceIds => this._doctorsService.getDoctorAvailableDays(this.doctorId, serviceIds))
        ).subscribe(result => {
            this.availableDays = result.map(r => r.date);
            this.availableDates = result;
            this.form.controls.date.enable();
        });

        this.form.controls.date.valueChanges.pipe(
            takeUntil(this._destroy$),
            tap(date => {
                if (!!date) {
                    this.form.controls.startHour.enable();
                } else {
                    this.form.controls.startHour.disable();
                }
                this.form.controls.startHour.setValue('');
            }),
            filter(date => !!date)
        ).subscribe(_ => {
            const hours = this.availableDates.find(vad =>
                vad.date.toISOString() === this.form.controls.date.value.toDate().toISOString()
            )?.hours;
            this.availableHours$.next(hours || []);
        });
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const visit = this.form.value as VisitForm;

            this._visitsService.addVisit(visit).pipe(
                takeUntil(this._destroy$),
                indicate(this.isSaving$)
            ).subscribe(_ => {
                this._toastr.success("Pomyślnie dokonano rezerwacji");
                this._router.navigateByUrl('/visits');
            });
        } else {
            this.form.markAllAsTouched();
        }
    }

    dateFilter = (d: Moment | null): boolean => {
        const date = (d || moment());
        const dateISO = date.toDate().toISOString();

        return this.minDate <= date && this.availableDays.find(dad => dad.toISOString() === dateISO) !== undefined;
    };
}
