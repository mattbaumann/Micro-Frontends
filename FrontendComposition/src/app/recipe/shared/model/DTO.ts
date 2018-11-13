import { LinkList } from "../../../utilities/Hateoas";

export interface RecipeListDTO {
  recipes : RecipeDTO[];
}

export interface RecipeDTO {
  id: number;
  name: String;
  _links: LinkList;
}
