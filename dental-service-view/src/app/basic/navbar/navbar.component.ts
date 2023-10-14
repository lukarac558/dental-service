import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
    selector: 'ds-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    @Input() isMobile: boolean | null;
    @Input() isMenuOpen: boolean | null;

    @Output() menuToggled = new EventEmitter<void>();

    isAuth$: Observable<boolean>;

    constructor(
        private _authService: AuthService
    ) { }

    ngOnInit(): void {
        this.isAuth$ = this._authService.isAuth$;
    }

    menuToggle(): void {
        this.menuToggled.next();
    }

    logout(): void {
        this._authService.logout();
    }
}
