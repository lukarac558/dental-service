import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, of, skipWhile, switchMap, tap } from 'rxjs';

import { AuthService } from '../services/auth.service';

export const NotAuthGuard: CanActivateFn = ():
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {

    const router: Router = inject(Router);
    const authService: AuthService = inject(AuthService);

    return authService.isLoading$.pipe(
        skipWhile(isLoading => isLoading),
        switchMap(_ => authService.isAuth$),
        switchMap(isAuth => of(!isAuth)),
        tap(isNotAuth => {
            if (!isNotAuth) {
                router.navigateByUrl('/account');
            }
        })
    );
}
