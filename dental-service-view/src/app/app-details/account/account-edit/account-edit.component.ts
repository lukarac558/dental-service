import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { Lookup } from 'src/app/core/models/lookup.model';
import { UserDetails, UserDetailsForm } from 'src/app/core/models/user.model';
import { LocationsService } from 'src/app/core/services/locations.service';
import { UsersService } from 'src/app/core/services/users.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { postalCodeRegex } from 'src/app/shared/regex/postal-code.regex';
import { phoneNumberValidator } from 'src/app/shared/validators/phone-number.validator';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-account-edit',
    templateUrl: './account-edit.component.html'
})
export class AccountEditComponent implements OnInit, OnDestroy {
    form: FormGroup;
    isSaving$ = new Subject<boolean>();
    userDetails$: Observable<UserDetails>;
    voivodeships$: Observable<Lookup[]>;

    private _destroy$ = new Subject<void>();

    constructor(
        private _usersSerivce: UsersService,
        private _fb: FormBuilder,
        private _toastr: ToastrService,
        private _router: Router,
        private _locationsService: LocationsService
    ) { }

    get addressFormGroup(): FormGroup {
        return this.form.controls.address as FormGroup;
    }

    ngOnInit(): void {
        this.form = this._fb.group<ControlsOf<UserDetailsForm>>({
            name: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
            surname: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
            personalId: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]),
            phoneNumber: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(8), Validators.maxLength(12), phoneNumberValidator]),
            address: this._fb.group({
                voivodeshipId: this._fb.control<number | null>(null, [Validators.required]),
                postalCode: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(6), Validators.maxLength(6), Validators.pattern(postalCodeRegex)]),
                city: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
                street: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(80)]),
                buildingNumber: this._fb.nonNullable.control('', [Validators.required, Validators.minLength(1), Validators.maxLength(8)])
            })
        });

        this.userDetails$ = this._usersSerivce.getCurrentUserDetails().pipe(
            tap(userDetails => this.form.patchValue(userDetails))
        );
        this.voivodeships$ = this._locationsService.getVoivodeships();
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const userDetails = this.form.value as UserDetailsForm;

            this._usersSerivce.updateUserDetails(userDetails).pipe(
                takeUntil(this._destroy$),
                indicate(this.isSaving$)
            ).subscribe({
                next: _ => {
                    this._toastr.success('Pomyślnie zapisano informacje.');
                    this._router.navigateByUrl('/account');
                },
                error: (error: HttpErrorResponse) => {
                    this._toastr.error(error.error.reasons.length > 0 ? error.error.reasons[0] : 'Nieprawidłowe dane.');
                }
            });
        } else {
            this.form.markAllAsTouched();
        }
    }
}
