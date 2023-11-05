import { inject } from '@angular/core';
import { CanDeactivateFn, UrlTree } from '@angular/router';
import { map, Observable, of, skipWhile, switchMap } from 'rxjs';
import { AccountEditComponent } from 'src/app/app-details/account/account-edit/account-edit.component';

import { AuthService } from '../services/auth.service';
import { UsersService } from '../services/users.service';

export const CanDeactivateAsUnregisteredGuard: CanDeactivateFn<AccountEditComponent> = ():
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {

    const authService: AuthService = inject(AuthService);
    const usersService: UsersService = inject(UsersService);

    return authService.isLoading$.pipe(
        skipWhile(isLoading => isLoading),
        switchMap(_ => authService.isAuth$),
        switchMap(isAuth => {
            if (isAuth) {
                return usersService.getCurrentUserDetails().pipe(
                    map(result => !!result.id)
                );
            } else {
                return of(true);
            }
        })
    );
}
