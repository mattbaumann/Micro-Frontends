export interface RecipeListHolder {
  _embedded: RecipeList;
}

export interface RecipeList {
  recipes : Array<Recipe>;
}

export interface Recipe {
  id: number;
  name: String;
}
