import React, { ChangeEvent, Component } from "react";
import { Button, CheckboxProps, Form, InputOnChangeData } from "semantic-ui-react";
import { Redirect } from "react-router";
import { KitchenDeviceBackend, KitchenDeviceDTO } from "../../dal/KitchenDevice";

export default class AddKitchenDevice extends Component<Properties, State> {

    constructor(props: Properties) {
        super(props);
        this.state = { kitchenDevice: { id: 0, name: "", function: "", available: false, _links: {} }, redirect: false };
        if (!!this.props.match.params.id) {
            this.props.backend
                .getDtoById(this.props.match.params.id)
                .then(value => this.setState({ kitchenDevice: value, redirect: false }));
        }
    }

    submit = () => {
        if (!!this.props.match.params.id) {
            this.props.backend.updateDto(this.state.kitchenDevice).then(() => this.setState({
                redirect: true
            }));
        } else {
            this.props.backend.addDto(this.state.kitchenDevice).then(() => this.setState({
                redirect: true
            }));
        }
    };

    changeOfName = (_event: ChangeEvent<HTMLInputElement>, data: InputOnChangeData) =>
        this.setState(state => {
            state.kitchenDevice.name = data.value;
            return state;
        });

    changeOfFunction = (_event: ChangeEvent<HTMLInputElement>, data: InputOnChangeData) =>
        this.setState(state => {
           state.kitchenDevice.function = data.value;
           return state;
        });

    changeOfAvailable = (_event: any, data: CheckboxProps) =>
        this.setState(state =>{
           state.kitchenDevice.available = data.checked as boolean;
           return state;
        });

    renderForm: () => React.ReactNode = () =>
        <Form onSubmit={this.submit}>
            <Form.Input label='Name' type='text' name='name' value={this.state.kitchenDevice.name}
                        onChange={this.changeOfName} />
            <Form.Input label='Date' type='date' name='function' value={this.state.kitchenDevice.function}
                        onChange={this.changeOfFunction} />
            <Form.Checkbox label='Available' name='available' checked={this.state.kitchenDevice.available}
                           onChange={this.changeOfAvailable} />
            <Button type='submit'>Submit</Button>
        </Form>;

    renderRedirect: () => React.ReactNode = () => (<Redirect to='/recipe/list'/>);

    render: () => React.ReactNode = () => this.state.redirect ? this.renderRedirect() : this.renderForm();
}

interface Properties {
    backend: KitchenDeviceBackend;
    match: Record<string, any>;
}

interface State {
    kitchenDevice: KitchenDeviceDTO;
    redirect: boolean;
}