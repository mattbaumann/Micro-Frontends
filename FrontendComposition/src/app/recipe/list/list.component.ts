import { Component, OnInit, ViewChild } from '@angular/core';
import { merge, of } from 'rxjs';

import {RecipeService} from '../shared/service/recipe.service'
import { RecipeDTO, RecipeListDTO } from '../shared/model/DTO'
import { MatDialog, MatPaginator, MatSnackBar, MatSort } from "@angular/material";
import { catchError, filter, mergeMap } from "rxjs/internal/operators";
import { LoggerService } from 'src/app/logger/LoggerService';
import { Logger } from 'src/app/logger/Logger';
import { HttpClient } from "@angular/common/http";
import { MultiResultHolder } from "../../utilities/Hateoas";
import { AddComponent } from "../add/add.component";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  recipes : RecipeDTO[] = [];
  columnsToDisplay: string[] = ['id', 'name'];
  isLoadingResults = false;
  _logger : Logger;

  constructor(private transfer : RecipeService, private snackBar : MatSnackBar, loggerService : LoggerService, private dialog: MatDialog) {
    this._logger = loggerService.getLogger("recipe.list");
  }

  openAddDialog() {
    let recipe : RecipeDTO = <RecipeDTO>{};
    const dialog = this.dialog.open(AddComponent, {data: recipe})
      .afterClosed()
      .pipe(filter(dto => dto != ""))
      .pipe(mergeMap(dto => this.transfer.addRecipe(dto)))
      .subscribe(dto => this.recipes.push(dto));
  }

  ngOnInit() {
    this._logger.trace("Init recipe.list component");
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    this.transfer.recipeUpdate.pipe(catchError(list => {
      this.snackBar.open("Error loading data.", "Close snackbar.", {duration: 500});
      return list;
    }));
    merge(this.paginator.page, this.sort.sortChange).pipe(mergeMap(value =>
      this.transfer.loadRecipesPaged(this.sort.active, this.sort.direction, this.paginator.pageIndex)
    )).subscribe(list => this.onDataReceived(list));
    this.transfer.loadRecipesPaged(this.sort.active, this.sort.direction, this.paginator.pageIndex).subscribe(list => this.onDataReceived(list));
  }

  onDataReceived(list : MultiResultHolder<RecipeListDTO> | {}) {
    this._logger.info("update Recipe list view");
    if(list == {}) return;
    let l = list as MultiResultHolder<RecipeListDTO>;
    this.recipes = l._embedded.recipes;
    this.paginator.pageIndex = l.page.number;
    this.paginator.pageSize = 10;
    this.isLoadingResults = false;
  }
}
