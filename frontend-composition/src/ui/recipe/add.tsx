import React, { ChangeEvent, Component } from "react";
import { Redirect } from "react-router-dom";
import { Button, Form, InputOnChangeData } from "semantic-ui-react";
import { RecipeBackend, RecipeDTO } from "../../dal/RecipeBackend";

export default class AddRecipe extends Component<Properties, State> {

    constructor(props: Properties) {
        super(props);
        this.state = { recipe: { id: 0, name: "", _links: {} }, redirect: false };
        if (!!this.props.match.params.id) {
            this.props.backend
                .getDtoById(this.props.match.params.id)
                .then(value => this.setState({ recipe: value, redirect: false }));
        }
    }

    submit = () => {
        if (!!this.props.match.params.id) {
            this.props.backend.updateDto(this.state.recipe).then(() => this.setState({
                redirect: true
            }));
        } else {
            this.props.backend.addDto(this.state.recipe).then(() => this.setState({
                redirect: true
            }));
        }
    };

    changeOfName = (_event: any, data: InputOnChangeData) =>
        this.setState(state => {
            state.recipe.name = data.value;
            return state;
        });

    renderForm = () => (
        <Form onSubmit={this.submit}>
            <Form.Input label='Name' type='text' name='name' value={this.state.recipe.name}
                        onChange={this.changeOfName}/>
            <Button type='submit'>Submit</Button>
        </Form>
    );

    renderRedirect: () => React.ReactNode = () => (<Redirect to='/recipe/list'/>);

    render() : React.ReactNode {
        return this.state.redirect ? this.renderRedirect() : this.renderForm();
    }
}

interface Properties {
    backend: RecipeBackend;
    match: Record<string, any>;
}

interface State {
    recipe: RecipeDTO;
    redirect: boolean;
}