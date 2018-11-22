import React, { ChangeEvent, Component } from "react";
import { Button, Form, InputOnChangeData } from "semantic-ui-react";
import { PurchaseListBackend, PurchaseListDTO } from "../../dal/PurchaseListBackend";
import { Redirect } from "react-router";

export default class AddPurchaseList extends Component<Properties, State> {

    submit = () => {
        if (!!this.props.match.params.id) {
            this.props.backend.updateDto(this.state.purchaseList).then(() => this.setState({
                redirect: true
            }));
        } else {
            this.props.backend.addDto(this.state.purchaseList).then(() => this.setState({
                redirect: true
            }));
        }
    };
    changeOfName = (_event: ChangeEvent<HTMLInputElement>, data: InputOnChangeData) =>
        this.setState(state => {
            state.purchaseList.name = data.value;
            return state;
        });
    changeOfDate = (_event: ChangeEvent<HTMLInputElement>, data: InputOnChangeData) =>
        this.setState(state => {
            state.purchaseList.date = data.value;
            return state;
        });
    renderForm: () => React.ReactNode = () =>
        <Form onSubmit={this.submit}>
            <Form.Input label='Name' type='text' name='name' value={this.state.purchaseList.name}
                        onChange={this.changeOfName}/>
            <Form.Input label='Date' type='date' name='date' value={this.state.purchaseList.date}
                        onChange={this.changeOfDate}/>
            <Button type='submit'>Submit</Button>
        </Form>;
    renderRedirect: () => React.ReactNode = () => (<Redirect to='/recipe/list'/>);
    render: () => React.ReactNode = () => this.state.redirect ? this.renderRedirect() : this.renderForm();

    constructor(props: Properties) {
        super(props);
        this.state = { purchaseList: { id: 0, name: "", date: "", _links: {} }, redirect: false };
        if (!!this.props.match.params.id) {
            this.props.backend
                .getDtoById(this.props.match.params.id)
                .then(value => this.setState({ purchaseList: value, redirect: false }));
        }
    }
}

interface Properties {
    backend: PurchaseListBackend;
    match: Record<string, any>;
}

interface State {
    purchaseList: PurchaseListDTO;
    redirect: boolean;
}