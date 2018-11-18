import { HateoasDTL, HateoasDTO} from './HateoasDTL';

export interface RecipeListDTO {
    recipes : RecipeDTO[];
}

export interface RecipeDTO extends HateoasDTO {
    id: number;
    name: string;
}

export class RecipeBackend extends HateoasDTL<RecipeDTO, RecipeListDTO> {
    constructor() {
        super("http://localhost:9601/api", "/recipes");
    }
}