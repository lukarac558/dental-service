export type PageCriteria = {
    page: number;
    pageSize: number;
};

export type CustomPageCriteria<T> = T & PageCriteria;

export type Page<T> = {
    itemsCount: number;
    items: T[];
};
