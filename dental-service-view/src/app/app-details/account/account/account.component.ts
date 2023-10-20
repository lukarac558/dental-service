import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Gender } from 'src/app/core/models/gender.model';
import { UserDetails } from 'src/app/core/models/user.model';
import { UsersService } from 'src/app/core/services/users.service';

@Component({
    selector: 'ds-account',
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
    userDetails$: Observable<UserDetails>;
    gender = Gender;

    constructor(
        private _usersSerivce: UsersService
    ) { }

    ngOnInit(): void {
        this.userDetails$ = this._usersSerivce.getUserDetails();
    }
}
