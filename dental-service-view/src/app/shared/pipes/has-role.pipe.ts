import { Pipe, PipeTransform } from '@angular/core';
import { Role } from 'src/app/core/models/role.model';

@Pipe({
    name: 'hasRole'
})
export class HasRolePipe implements PipeTransform {
    transform(roles: string[], role: Role): boolean {
        return roles.includes(role);
    }
}
