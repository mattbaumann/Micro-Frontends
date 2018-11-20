import { HateoasDTL, HateoasDTO } from './HateoasDTL';

export interface KitchenDeviceDTOList {
    kitchenDevices: KitchenDeviceDTO[];
}

export interface KitchenDeviceDTO extends HateoasDTO {
    id: number;
    available: boolean;
    function: string;
    name: string;
}

export class KitchenDeviceBackend extends HateoasDTL<KitchenDeviceDTO, KitchenDeviceDTOList> {
    constructor() {
        super("http://localhost:9603/api", "/kitchenDevices");
    }
}