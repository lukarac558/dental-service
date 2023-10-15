import { Injectable } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';

@Injectable()
export class Paginator extends MatPaginatorIntl {
    override itemsPerPageLabel = 'Liczba elementów:';
    override nextPageLabel = 'Następna strona';
    override previousPageLabel = 'Poprzednia strona'

    override getRangeLabel = (page: number, pageSize: number, length: number) => {
        if (pageSize === 0 || length === 0) {
            return `0 / ${length}`;
        }
        const start = page * pageSize;
        const end = Math.min(start + pageSize, length)
        return `${start + 1} - ${end} / ${length}`;
    };
}
