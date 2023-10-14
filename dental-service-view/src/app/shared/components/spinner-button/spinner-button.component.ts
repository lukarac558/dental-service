import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'ds-spinner-button',
    templateUrl: './spinner-button.component.html',
    styleUrls: ['./spinner-button.component.scss']
})
export class SpinnerButtonComponent {
    @Input() isLoading: boolean | null;

    @Output() clicked = new EventEmitter<void>();

    onClick(): void {
        if (!this.isLoading) {
            this.clicked.emit();
        }
    }
}
