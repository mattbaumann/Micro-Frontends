import { Component, OnInit } from '@angular/core';

import {RecipeService} from '../shared/service/recipe.service'
import {Recipe} from '../shared/model/recipe'

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  recipes : Recipe[];

  constructor(private transfer : RecipeService) {
    transfer.loadRecipes().subscribe(data => this.recipes = data._embedded.recipes );
  }

  ngOnInit() {

  }
}
