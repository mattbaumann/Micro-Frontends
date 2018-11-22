import React, { Component } from 'react';
import { BrowserRouter, Route } from 'react-router-dom';

import MainMenu from './fragments/MainMenu';
import Home from './fragments/Home';
import AddPurchaseList from './purchase-list/add';
import './App.css';
import { PurchaseListBackend } from "../dal/PurchaseListBackend";
import ListPurchaseLists from "./purchase-list/list";

interface Props {

}

interface State {
    purchaseList: PurchaseListBackend;
}

class App extends Component<Props, State> {

    constructor(props: Readonly<Props>) {
        super(props);
        this.state = {
            purchaseList: new PurchaseListBackend()
        };
    }

    render() {
        return (
            <BrowserRouter>
                <div>
                    <MainMenu/>
                    <Route exact path='/' component={Home}/>
                    <Route path='/purchaseList/add'
                           render={(props) => <AddPurchaseList {...props} backend={this.state.purchaseList}/>}/>
                    <Route exact path='/purchaseList/add/:id'
                           render={(props) => <AddPurchaseList {...props} backend={this.state.purchaseList}/>}/>
                    <Route path='/purchaseList/list'
                           render={(props) => <ListPurchaseLists {...props} backend={this.state.purchaseList}/>}/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
