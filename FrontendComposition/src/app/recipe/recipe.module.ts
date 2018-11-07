import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';

import {RecipeService} from './shared/service/recipe.service'

@NgModule({
  declarations: [ListComponent],
  imports: [
    CommonModule
  ],
  providers: [
    RecipeService
  ]
})
export class RecipeModule { }
