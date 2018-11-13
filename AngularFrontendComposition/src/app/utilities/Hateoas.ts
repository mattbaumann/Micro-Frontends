
export interface MultiResultHolder<T> {
    _embedded: T;
    _links: LinkList;
    page: PagingInformation;
}

export interface PagingInformation {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
}

export interface LinkList {
    self: LinkItem;
}

export interface LinkItem {
    href : string;
    // maybe undefined -> false
    templated: boolean;
}
