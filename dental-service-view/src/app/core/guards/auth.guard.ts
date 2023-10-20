import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { Observable, skipWhile, switchMap, tap } from 'rxjs';

import { AuthService } from '../services/auth.service';

export const AuthGuard: CanActivateFn = ():
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {

    const router: Router = inject(Router);
    const authService: AuthService = inject(AuthService);

    return authService.isLoading$.pipe(
        skipWhile(isLoading => isLoading),
        switchMap(_ => authService.isAuth$),
        tap(isAuth => {
            if (!isAuth) {
                router.navigateByUrl('/');
            }
        })
    );
}
