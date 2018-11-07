import { Injectable, EventEmitter } from '@angular/core';
import {HttpClient } from '@angular/common/http';
import { RecipeListHolder, RecipeList } from "../model/recipe";
import {Observable} from "rxjs/index";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private static recipeUrl = 'http://localhost:9601/api/recipes';

  public recipeUpdate : EventEmitter<RecipeList> = new EventEmitter<RecipeList>(true);

  private cache: RecipeList = null;

  constructor(private http: HttpClient) { }

  public loadRecipes () : Observable<RecipeListHolder> {
      return this.http.get<RecipeListHolder>(RecipeService.recipeUrl)
  }

  public get recipes() : RecipeList {return this.cache; }
}
