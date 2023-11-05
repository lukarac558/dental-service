import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Gender } from 'src/app/core/models/gender.model';
import { Lookup } from 'src/app/core/models/lookup.model';
import { Role } from 'src/app/core/models/role.model';
import { UserDetails } from 'src/app/core/models/user.model';
import { LocationsService } from 'src/app/core/services/locations.service';
import { UsersService } from 'src/app/core/services/users.service';

@Component({
    selector: 'ds-account',
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
    userDetails$: Observable<UserDetails>;
    gender = Gender;
    role = Role;
    voivodeships$: Observable<Lookup[]>;

    constructor(
        private _usersSerivce: UsersService,
        private _locationsService: LocationsService
    ) { }

    ngOnInit(): void {
        this.userDetails$ = this._usersSerivce.getCurrentUserDetails();
        this.voivodeships$ = this._locationsService.getVoivodeships();
    }
}
