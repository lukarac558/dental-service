import { AbstractControl, ValidationErrors } from '@angular/forms';

import { phoneNumberRegex1, phoneNumberRegex2, phoneNumberRegex3 } from '../regex/phone-number.regex';

export function phoneNumberValidator(control: AbstractControl<string>): ValidationErrors | null {
    const { value } = control;
    if (!value) {
        return null;
    }
    return (
        phoneNumberRegex1.test(value) ||
        phoneNumberRegex2.test(value) ||
        phoneNumberRegex3.test(value)
    ) ? null : { regex: true };
}
