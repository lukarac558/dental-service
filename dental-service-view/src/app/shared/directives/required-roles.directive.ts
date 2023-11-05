import { Directive, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { Role } from 'src/app/core/models/role.model';
import { AuthService } from 'src/app/core/services/auth.service';

@Directive({
    selector: '[dsRequiredRoles]'
})
export class RequiredRolesDirective implements OnInit, OnDestroy {
    @Input() dsRequiredRoles: Role[];

    private _destroy$ = new Subject<void>();

    constructor(
        private _viewContainerRef: ViewContainerRef,
        private _templateRef: TemplateRef<any>,
        private _authService: AuthService
    ) { }

    ngOnInit(): void {
        this._authService.hasUserRequiredRole(this.dsRequiredRoles).pipe(
            takeUntil(this._destroy$)
        ).subscribe(result => {
            if (result) {
                this._viewContainerRef.createEmbeddedView(this._templateRef);
            } else {
                this._viewContainerRef.clear();
            }
        });
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }
}
