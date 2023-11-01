import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject, takeUntil, tap } from 'rxjs';
import { Gender } from 'src/app/core/models/gender.model';
import { UserDetails, UserDetailsForm } from 'src/app/core/models/user.model';
import { UsersService } from 'src/app/core/services/users.service';
import { indicate } from 'src/app/shared/operators/indicate';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-account-edit',
    templateUrl: './account-edit.component.html'
})
export class AccountEditComponent implements OnInit, OnDestroy {
    userDetails$: Observable<UserDetails>;
    form: FormGroup;
    isSaving$ = new Subject<boolean>();
    gender = Gender;

    private _destroy$ = new Subject<void>();

    constructor(
        private _usersSerivce: UsersService,
        private _fb: FormBuilder,
        private _toastr: ToastrService,
        private _router: Router
    ) { }

    ngOnInit(): void {
        this.form = this._fb.group<ControlsOf<UserDetailsForm>>({
            firstName: this._fb.nonNullable.control('', [Validators.required]),
            lastName: this._fb.nonNullable.control('', [Validators.required]),
            email: this._fb.nonNullable.control('', [Validators.required]),
            phoneNumber: this._fb.nonNullable.control('', [Validators.required]),
            zipCode: this._fb.nonNullable.control('', [Validators.required]),
            city: this._fb.nonNullable.control('', [Validators.required]),
            address: this._fb.nonNullable.control('', [Validators.required]),
            gender: this._fb.nonNullable.control(Gender.Male, [Validators.required])
        });

        this.userDetails$ = this._usersSerivce.getUserDetails().pipe(
            tap(userDetails => this.form.patchValue(userDetails))
        );
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    save(): void {
        if (this.form.valid) {
            const userDetails = this.form.value as UserDetails;

            this._usersSerivce.updateUserDetails(userDetails).pipe(
                takeUntil(this._destroy$),
                indicate(this.isSaving$)
            ).subscribe(_ => {
                this._toastr.success('Pomyślnie zapisano informacje.');
                this._router.navigateByUrl('/account');
            })
        } else {
            this.form.markAllAsTouched();
        }
    }
}
