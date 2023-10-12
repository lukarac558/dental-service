import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'ds-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
    @Input() isMobile: boolean | null;
    @Input() isMenuOpen: boolean | null;

    @Output() menuToggled = new EventEmitter<void>();

    menuToggle(): void {
        this.menuToggled.next();
    }
}
