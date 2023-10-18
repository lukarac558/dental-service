import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Gender } from 'src/app/core/models/gender.model';
import { RegisterForm } from 'src/app/core/models/login-register.model';
import { Role } from 'src/app/core/models/role.model';
import { InitUserDetailsForm } from 'src/app/core/models/applicationUser.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
    registerForm: FormGroup;
    userDetailsForm: FormGroup;
    gender = Gender;
    role = Role;

    constructor(
        private _authService: AuthService,
        private _fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.registerForm = this._fb.group<ControlsOf<RegisterForm>>({
            email: this._fb.nonNullable.control('', [Validators.required]),
            password: this._fb.nonNullable.control('', [Validators.required]),
            repeatPassword: this._fb.nonNullable.control('', [Validators.required]),
        });

        this.userDetailsForm = this._fb.group<ControlsOf<InitUserDetailsForm>>({
            firstName: this._fb.nonNullable.control('', [Validators.required]),
            lastName: this._fb.nonNullable.control('', [Validators.required]),
            phoneNumber: this._fb.nonNullable.control('', [Validators.required]),
            zipCode: this._fb.nonNullable.control('', [Validators.required]),
            city: this._fb.nonNullable.control('', [Validators.required]),
            address: this._fb.nonNullable.control('', [Validators.required]),
            gender: this._fb.nonNullable.control(Gender.Male, [Validators.required]),
            role: this._fb.nonNullable.control(Role.Patient, [Validators.required]),
        });
    }

    retgister(): void {
        if (this.registerForm.valid && this.userDetailsForm.valid) {
            const credentials = this.registerForm.value as RegisterForm;
            const userDetails = this.userDetailsForm.value as InitUserDetailsForm;

            this._authService.register(credentials, userDetails);
        } else {
            this.registerForm.markAllAsTouched();
            this.userDetailsForm.markAllAsTouched();
        }
    }
}
