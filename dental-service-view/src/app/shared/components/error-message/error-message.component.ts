import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
    selector: 'ds-error-message',
    templateUrl: './error-message.component.html'
})
export class ErrorMessageComponent {
    @Input() control: AbstractControl;
    private _errorText = '';

    getError() {
        if (this.control) {
            if (this.control.hasError("required")) {
                this._errorText = 'Pole jest wymagane.';
            } else if (this.control.hasError("maxlength")) {
                this._errorText = 'Pole posiada za dużo tekstu.';
            } else if (this.control.hasError("min")) {
                this._errorText = 'Pole posiada za małą wartość.';
            } else if (this.control.hasError("max")) {
                this._errorText = 'Pole posiada za dużą wartość.';
            } else if (this.control.hasError("pattern")) {
                this._errorText = 'Pole posiada nieprawidłowy format.';
            } else if (this.control.hasError("matDatepickerParse")) {
                this._errorText = 'Nieprawidłowy format daty.';
            } else if (this.control.hasError("dateRangeInvalid") || this.control.hasError("matStartDateInvalid")) {
                this._errorText = 'Nieprawidłowy zakres dat.';
            } else if (this.control.hasError("email")) {
                this._errorText = 'Nieprawidłowy format e-mail.';
            } else {
                this._errorText = '';
            }
        }
        return this._errorText;
    }
}
