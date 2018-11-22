import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {KitchenDeviceModule} from './kitchen-device/kitchen-device.module';
import {PurchaseListModule} from './purchase-list/purchase-list.module';
import {RecipeModule} from './recipe/recipe.module';
import { AppLoggingModule } from './app-logging.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,

    // Service Modules
    AppLoggingModule,

    // Project Modules
    KitchenDeviceModule,
    PurchaseListModule,
    RecipeModule,

    // last module
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
