import React, { Component } from "react";
import { Button, Icon, Pagination, PaginationProps, Table } from "semantic-ui-react";
import { RecipeBackend, RecipeDTO, RecipeListDTO } from "../../dal/RecipeBackend";
import { Redirect } from "react-router";
import { MultiResultHolder } from "../../dal/HateoasDTL";

export default class ListRecipes extends Component<Properties, State> {

    constructor(props: Properties) {
        super(props);
        this.state = {
            recipes: {
                _embedded: { recipes: [] },
                page: { number: 0, size: 0, totalElements: 0, totalPages: 0 },
                _links: {},
            },
            redirectPath: '',
            page: 0
        };
        this.props.backend.defaultPageDto().then(value => this.setState({ recipes: value }));
    }

    navigateEdit = (id: number) => () => this.setState({ redirectPath: `/recipe/add/${id}` });

    requestDeletion = (id: number) => () =>
        this.props.backend.deleteDtoById(id)
            .then(() => this.props.backend.defaultPageDto())
            .then(value => this.setState({ recipes: value }));

    pageChange = (event: any, data: PaginationProps) =>
        this.props.backend.pageDto(data.activePage as number)
            .then(value => this.setState({ recipes: value }));

    renderRedirect(): React.ReactNode {
        if (this.state.redirectPath != '') {
            return (<Redirect to={this.state.redirectPath}/>);
        }
    }

    renderItem = (recipe: RecipeDTO) =>
        <Table.Row key={recipe.id}>
            {this.renderRedirect()}
            <Table.Cell>
                {recipe.id}
            </Table.Cell>
            <Table.Cell>
                {recipe.name}
            </Table.Cell>
            <Table.Cell>
                <Button icon='pencil' content='Edit' onClick={this.navigateEdit(recipe.id)}/>
                <Button icon='trash' content='Delete' onClick={this.requestDeletion(recipe.id)}/>
            </Table.Cell>
        </Table.Row>;

    renderHeader = () =>
        <Table.Row>
            <Table.HeaderCell>Id</Table.HeaderCell>
            <Table.HeaderCell>Name</Table.HeaderCell>
            <Table.HeaderCell>Actions</Table.HeaderCell>
        </Table.Row>;

    renderPagination = () =>
        <Table.Row>
            <Table.HeaderCell colSpan='3'>
                <Pagination
                    totalPages={this.state.recipes.page.totalPages}
                    activePage={this.state.recipes.page.number}
                    ellipsisItem={{ content: <Icon name='ellipsis horizontal'/>, icon: true }}
                    firstItem={null}
                    lastItem={null}
                    prevItem={{ content: <Icon name='angle left'/>, icon: true }}
                    nextItem={{ content: <Icon name='angle right'/>, icon: true }}
                    onPageChange={this.pageChange}
                />
            </Table.HeaderCell>
        </Table.Row>;

    render = () =>
        <Table celled>
            <Table.Header>
                {this.renderHeader()}
            </Table.Header>
            <Table.Body>
                {this.state.recipes._embedded.recipes.map(recipe => this.renderItem(recipe))}
            </Table.Body>
            <Table.Footer>
                {this.renderPagination()}
            </Table.Footer>
        </Table>;
}

interface Properties {
    backend : RecipeBackend;
}

interface State {
    recipes: MultiResultHolder<RecipeListDTO>;
    redirectPath: string;
    page: number;
}
