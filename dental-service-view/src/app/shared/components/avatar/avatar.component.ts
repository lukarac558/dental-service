import { Component, Input, OnInit } from '@angular/core';
import { Gender } from 'src/app/core/models/gender.model';
import { Role } from 'src/app/core/models/role.model';

@Component({
    selector: 'ds-avatar',
    templateUrl: './avatar.component.html',
    styleUrls: ['./avatar.component.scss']
})
export class AvatarComponent implements OnInit {
    @Input() gender: number = Gender.Male;
    @Input() role: number = Role.Patient;
    @Input() avatarSizePx = 50;

    imagePath: string;

    ngOnInit(): void {
        if (this.gender == Gender.Male && this.role == Role.Doctor) {
            this.imagePath = '/assets/images/account/dentist-m.png';
        } else if (this.gender == Gender.Female && this.role == Role.Doctor) {
            this.imagePath = '/assets/images/account/dentist-w.png';
        } else if (this.gender == Gender.Male && this.role == Role.Patient) {
            this.imagePath = '/assets/images/account/patient-m.png';
        } else if (this.gender == Gender.Female && this.role == Role.Patient) {
            this.imagePath = '/assets/images/account/patient-w.png';
        }
    }
}
