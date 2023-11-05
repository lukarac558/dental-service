import { Pipe, PipeTransform } from '@angular/core';
import { Lookup } from 'src/app/core/models/lookup.model';

@Pipe({
    name: 'voivodeship'
})
export class VoivodeshipPipe implements PipeTransform {
    transform(id: number | null, voivodeships: Lookup[] | null): string {
        return voivodeships?.find(v => v.id === id)?.name || '';
    }
}
