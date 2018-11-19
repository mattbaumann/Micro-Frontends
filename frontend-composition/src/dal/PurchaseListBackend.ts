import { HateoasDTL, HateoasDTO } from './HateoasDTL';

export interface PurchaseListDTOList {
    purchaseLists: PurchaseListDTO[];
}

export interface PurchaseListDTO extends HateoasDTO {
    id : number;
    name : string;
    date: string;
}

export interface PurchaseListItemDTO extends HateoasDTO {
    id : number;
    name : string;
}

export class PurchaseListBackend extends HateoasDTL<PurchaseListDTO, PurchaseListDTOList> {
    constructor() {
        super("http://localhost:9602/api", "/purchaseLists");
    }
}