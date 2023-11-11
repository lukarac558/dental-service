import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';
import { Moment } from 'moment';

@Pipe({
    name: 'stringToDate'
})
export class StringToDatePipe implements PipeTransform {
    transform(value: string): Moment {
        return moment(value);
    }
}
