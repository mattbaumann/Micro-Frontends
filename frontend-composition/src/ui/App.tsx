import React, { Component } from 'react';
import { BrowserRouter, Route, Router } from 'react-router-dom';

import MainMenu from './fragments/MainMenu';
import Home from './fragments/Home';
import AddPurchaseList from './purchase-list/add';
import './App.css';
import AddRecipe from "./recipe/add";
import ListRecipes from "./recipe/list";
import { RecipeBackend } from "../dal/RecipeBackend";
import { PurchaseListBackend } from "../dal/PurchaseListBackend";
import ListPurchaseLists from "./purchase-list/list";
import { KitchenDeviceBackend } from "../dal/KitchenDevice";
import AddKitchenDevice from "./kitchen-device/add";
import KitchenDeviceLists from "./kitchen-device/list";

interface Props {

}

interface State {
    recipe : RecipeBackend;
    purchaseList: PurchaseListBackend;
    kitchenDevice: KitchenDeviceBackend;
}

class App extends Component<Props, State> {

    constructor(props: Readonly<Props>) {
        super(props);
        this.state = {
            recipe: new RecipeBackend(),
            purchaseList: new PurchaseListBackend(),
            kitchenDevice: new KitchenDeviceBackend()
        };
    }

    render() {
    return (
        <BrowserRouter>
            <div>
                <MainMenu />
                <Route exact path='/' component={Home} />
                <Route path='/kitchenDevice/add' render={(props) =>     <AddKitchenDevice {...props} backend={this.state.kitchenDevice} />} />
                <Route exact path='/recipe/add/:id' render={(props) =>  <AddKitchenDevice {...props} backend={this.state.kitchenDevice} />} />
                <Route path='/kitchenDevice/list' render={(props) =>    <KitchenDeviceLists {...props} backend={this.state.kitchenDevice} />} />
                <Route path='/purchaseList/add' render={(props) =>      <AddPurchaseList {...props} backend={this.state.purchaseList} />} />
                <Route exact path='/purchaseList/add/:id' render={(props) => <AddPurchaseList {...props} backend={this.state.purchaseList} />} />
                <Route path='/purchaseList/list' render={(props) =>     <ListPurchaseLists {...props} backend={this.state.purchaseList} />} />
                <Route exact path='/recipe/add' render={(props) =>      <AddRecipe {...props} backend={this.state.recipe} />} />
                <Route exact path='/recipe/add/:id' render={(props) =>  <AddRecipe {...props} backend={this.state.recipe} />} />
                <Route path='/recipe/list' render={(props) =>     <ListRecipes {...props} backend={this.state.recipe} />} />
            </div>
        </BrowserRouter>
    );
  }
}

export default App;
