import { LinkList } from "./Hateoas";

export interface RecipeListDTO {
  recipes : RecipeDTO[];
}

export interface RecipeDTO {
  id: number;
  name: String;
  _links: LinkList;
}
