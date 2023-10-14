import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginForm } from 'src/app/core/models/login-register.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { ControlsOf } from 'src/main';

@Component({
    selector: 'ds-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    form: FormGroup;

    constructor(
        private _authService: AuthService,
        private _fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.form = this._fb.group<ControlsOf<LoginForm>>({
            email: this._fb.nonNullable.control('', [Validators.required]),
            password: this._fb.nonNullable.control('', [Validators.required]),
        });
    }

    login(): void {
        if (this.form.valid) {
            const credentials = this.form.value as LoginForm;

            this._authService.login(credentials);
        } else {
            this.form.markAllAsTouched();
        }
    }
}
