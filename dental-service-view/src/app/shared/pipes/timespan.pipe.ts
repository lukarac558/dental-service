import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
    name: 'timespan'
})
export class TimespanPipe implements PipeTransform {
    transform(value: string): string {
        const duration = moment.duration(value, 'hours');
        const formatted = moment.utc(duration.asMilliseconds()).format("HH:mm")
        return formatted;
    }
}
