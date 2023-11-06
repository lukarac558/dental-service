import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export type ConfirmationModalData = {
    title: string;
    description: string;
};

@Component({
    selector: 'ds-confirmation-modal',
    templateUrl: './confirmation-modal.component.html'
})
export class ConfirmationModalComponent {
    title: string;
    description: string;

    constructor(
        private _dialogRef: MatDialogRef<ConfirmationModalComponent>,
        @Inject(MAT_DIALOG_DATA) private _data: ConfirmationModalData,
    ) {
        this.title = this._data.title;
        this.description = this._data.description;
    }

    confirm(): void {
        this._dialogRef.close(true);
    }
}
