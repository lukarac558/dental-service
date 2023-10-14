import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of, switchMap, tap } from 'rxjs';

import { AuthService } from '../services/auth.service';

export const NotAuthGuard: CanActivateFn = ():
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {

    const router: Router = inject(Router);
    const authService: AuthService = inject(AuthService);

    return authService.isAuth$.pipe(
        switchMap(isAuth => of(!isAuth)),
        tap(isNotAuth => {
            if (!isNotAuth) {
                router.navigateByUrl('');
            }
        })
    );
}
