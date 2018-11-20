import React, { Component } from 'react';
import { BrowserRouter, Route } from 'react-router-dom';

import MainMenu from './fragments/MainMenu';
import Home from './fragments/Home';
import './App.css';
import { KitchenDeviceBackend } from "../dal/KitchenDevice";
import AddKitchenDevice from "./kitchen-device/add";
import KitchenDeviceLists from "./kitchen-device/list";

interface Props {

}

interface State {
    kitchenDevice: KitchenDeviceBackend;
}

class App extends Component<Props, State> {

    constructor(props: Readonly<Props>) {
        super(props);
        this.state = {
            kitchenDevice: new KitchenDeviceBackend()
        };
    }

    render() {
        return (
            <BrowserRouter>
                <div>
                    <MainMenu/>
                    <Route exact path='/' component={Home}/>
                    <Route path='/kitchenDevice/add'
                           render={(props) => <AddKitchenDevice {...props} backend={this.state.kitchenDevice}/>}/>
                    <Route exact path='/kitchenDevice/add/:id'
                           render={(props) => <AddKitchenDevice {...props} backend={this.state.kitchenDevice}/>}/>
                    <Route path='/kitchenDevice/list'
                           render={(props) => <KitchenDeviceLists {...props} backend={this.state.kitchenDevice}/>}/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
