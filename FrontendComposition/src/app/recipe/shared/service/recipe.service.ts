import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { RecipeDTO, RecipeListDTO } from "../model/DTO";
import { catchError, debounceTime, map, tap, timeout } from "rxjs/internal/operators";
import { Observable } from "rxjs/index";
import { MultiResultHolder } from "../../../utilities/Hateoas";
import { LoggerService } from "../../../logger/LoggerService";
import { Logger } from "../../../logger/Logger";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private static recipeUrl = 'http://localhost:9601/api/recipes';

  public recipeUpdate : EventEmitter<RecipeListDTO> = new EventEmitter<RecipeListDTO>(true);

  private cache: RecipeListDTO = null;

  private readonly _logger : Logger;

  constructor(private http: HttpClient, loggerService : LoggerService) {
    this._logger = loggerService.getLogger("recipe.service");
  }

  public loadRecipesPaged(sort: string = "id", direction: string = "asc", page: number = 0) {
    this._logger.info(`Loading Recipes with sort ${sort}, ${direction} and page ${page}`);

    const params = new HttpParams()
      .append("sort", sort + "," + direction)
      .append("page", page.toString());

    return this.http.get<MultiResultHolder<RecipeListDTO>>(RecipeService.recipeUrl, { params: params });
  }

  public addRecipe(dto : RecipeDTO) {
    return this.http.post<RecipeDTO>(RecipeService.recipeUrl, dto);
  }

  public loadRecipe(idOrURL: number) {

  }

  public get recipes() : RecipeListDTO {return this.cache; }
}
