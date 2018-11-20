import React, { Component } from 'react';
import { BrowserRouter, Route } from 'react-router-dom';

import MainMenu from './fragments/MainMenu';
import Home from './fragments/Home';
import './App.css';
import AddRecipe from "./recipe/add";
import ListRecipes from "./recipe/list";
import { RecipeBackend } from "../dal/RecipeBackend";
import { PurchaseListBackend } from "../dal/PurchaseListBackend";
import { KitchenDeviceBackend } from "../dal/KitchenDevice";

interface Props {

}

interface State {
    recipe: RecipeBackend;
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
                    <MainMenu/>
                    <Route exact path='/' component={Home}/>
                    <Route exact path='/recipe/add'
                           render={(props) => <AddRecipe {...props} backend={this.state.recipe}/>}/>
                    <Route exact path='/recipe/add/:id'
                           render={(props) => <AddRecipe {...props} backend={this.state.recipe}/>}/>
                    <Route path='/recipe/list'
                           render={(props) => <ListRecipes {...props} backend={this.state.recipe}/>}/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
