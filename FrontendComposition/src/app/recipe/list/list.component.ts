import { Component, OnInit } from '@angular/core';

import {RecipeService} from '../shared/service/recipe.service'
import {RecipeList} from '../shared/model/recipe'

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  recipes : RecipeList;

  constructor(private transfer : RecipeService) {
    transfer.loadRecipes().subscribe(data => this.recipes = data._embedded );
  }

  ngOnInit() {

  }
}
