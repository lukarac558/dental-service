export type PageCriteria = {
    pageIndex: number;
    pageSize: number;
};

export type CustomPageCriteria<T> = T & PageCriteria;

export type Page<T> = {
    itemsCount: number;
    items: T[];
};
