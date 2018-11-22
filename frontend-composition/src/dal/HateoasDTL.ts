import * as $ from 'jquery';

export interface HateoasDTO extends LinksHolder, Record<string, string | number | boolean | Record<string, LinkItem>> { }

export interface MultiResultHolder<T> extends LinksHolder {
    _embedded: T;
    page: PagingInformation;
}

export interface LinksHolder {
    _links: Record<string, LinkItem>;
}

export interface PagingInformation {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
}

export interface LinkItem {
    href: string;
    templated: boolean;
}

export enum HTTPMethod {
    GET = "GET",
    PATCH = "PATCH",
    POST = "POST",
    PUT = "PUT",
    DELETE = "DELETE"
}

export enum Direction {
    ASC = "asc",
    DSC = "desc"
}

export class SortState {
    private static readonly DEFAULT_COLUMN = 'id';
    private static readonly ASCENDING_DIRECTION = 'ascending';
    private static readonly DESCENDING_DIRECTION = 'descending';

    column: string;
    direction: Direction;

    constructor(column ?: string, direction?: Direction) {
        this.column = column || SortState.DEFAULT_COLUMN;
        this.direction = direction || Direction.ASC;
    }

    getLongDirection() {
        return this.direction == Direction.ASC ? SortState.ASCENDING_DIRECTION : SortState.DESCENDING_DIRECTION;
    }

    toString = () => `${this.column},${this.direction}`;

    shouldReverseDirection(reverse : boolean) {
        if (reverse) {
            this.direction = this.direction == Direction.ASC ? Direction.DSC : Direction.ASC;
        }
    }
}


export abstract class HateoasDTL<E extends HateoasDTO, L> {

    private static readonly SELF_LINK = "self";
    private static readonly DEFAULT_PAGE = 0;

    protected constructor(private readonly apiUrl: string, private readonly entityName: string) { }

    private static sendRequestWithUrl<T>(method: HTTPMethod,
                                          url: string,
                                          params ?: Record<string, string | number | boolean | Record<string, LinkItem>>) : Promise<T> {
        if (method == HTTPMethod.GET)
            return $.ajax({
                type: method,
                url: url + (
                    params != undefined ? "?" + $.param(params as object) : '' // Types manually checked
                ),
                dataType: "json",
                contentType: "application/json; charset=utf-8"
            });
        else
            return $.ajax({
                type: method,
                url: url,
                data: JSON.stringify(params),
                dataType: "json",
                contentType: "application/json; charset=utf-8"
            });
    }

    sendRequest<T>(method: HTTPMethod,
                   path: string,
                   params ?: Record<string, string | number | boolean | Record<string, LinkItem>>) {
        return HateoasDTL.sendRequestWithUrl<T>(method, this.apiUrl + path, params);
    }

    static sendRequestWithDtoLink<T>(method: HTTPMethod,
                                     dto: LinksHolder,
                                     link: string, params ?: Record<string, string | number | boolean | Record<string, LinkItem>>) {
        return HateoasDTL.sendRequestWithUrl<T>(method, dto._links[ link ].href, params);
    }

    defaultPageDto(sorting?: SortState): Promise<MultiResultHolder<L>> {
        return this.pageDto(HateoasDTL.DEFAULT_PAGE, sorting);
    }

    pageDto(page: number, sorting?: SortState): Promise<MultiResultHolder<L>> {
        let sort = sorting || new SortState();
        return this.sendRequest(HTTPMethod.GET, this.entityName, { page: page, sort: sort.toString() });
    }

    getDto(dto: E): Promise<E> {
        return HateoasDTL.sendRequestWithDtoLink<E>(HTTPMethod.GET, dto, HateoasDTL.SELF_LINK);
    }

    getDtoById(id: number): Promise<E> {
        return this.sendRequest(HTTPMethod.GET, this.entityName + "/" + id);
    }

    addDto(dto: E): Promise<E> {
        return this.sendRequest(HTTPMethod.POST, this.entityName, dto);
    }

    updateDto(dto: E): Promise<E> {
        return HateoasDTL.sendRequestWithDtoLink<E>(HTTPMethod.PUT, dto, HateoasDTL.SELF_LINK, dto);
    }

    deleteDto(dto: E): Promise<E> {
        return HateoasDTL.sendRequestWithDtoLink<E>(HTTPMethod.DELETE, dto, HateoasDTL.SELF_LINK, dto);
    }

    deleteDtoById(id: number): Promise<E> {
        return this.sendRequest(HTTPMethod.DELETE, this.entityName + "/" + id);
    }
}