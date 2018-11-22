import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';

import {
  MatToolbarModule,
  MatMenuModule,
  MatButtonModule,
  MatIconModule,
  MatTableModule,
  MatProgressSpinnerModule,
  MatPaginatorModule,
  MatSortModule,
  MatSnackBarModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule
} from "@angular/material";

import {RecipeService} from './shared/service/recipe.service';
import { AddComponent } from './add/add.component'
import { ReactiveFormsModule } from "@angular/forms";

@NgModule({
  declarations: [ListComponent, AddComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,

    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule,
    MatDialogModule,
  ],
  providers: [
    RecipeService
  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,

    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule,
    MatDialogModule,
  ],
  entryComponents: [
    AddComponent
  ]
})
export class RecipeModule { }
