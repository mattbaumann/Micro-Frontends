import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { RecipeDTO } from "../shared/model/DTO";
import { FormControl } from "@angular/forms";

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent implements OnInit {

  name = new FormControl('A new recipe').registerOnChange();
  _name : string = "";

  constructor(private dialogRef : MatDialogRef<AddComponent>, @Inject(MAT_DIALOG_DATA) private data : RecipeDTO) { }

  onCancel() {
    this.dialogRef.close(null);
  }

  onSubmit() {
    this.dialogRef.close(this._name.)
  }

  ngOnInit() {
  }

}
