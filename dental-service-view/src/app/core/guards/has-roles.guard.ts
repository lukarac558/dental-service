import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, UrlTree } from '@angular/router';
import { from, Observable, of, switchMap } from 'rxjs';

import { Role } from '../models/role.model';
import { AuthService } from '../services/auth.service';

export const HasRolesGuard: CanActivateFn = (route: ActivatedRouteSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {

    const router: Router = inject(Router);
    const authService: AuthService = inject(AuthService);

    const roles = Object.values(route.data) as Role[] | null || [];

    return authService.hasUserRequiredRole(roles).pipe(
        switchMap(hasRoles => {
            if (hasRoles) {
                return of(true)
            }
            return from(router.navigateByUrl('/'));
        })
    );
}
