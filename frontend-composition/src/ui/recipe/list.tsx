import React, { Component } from "react";
import { Button, Dimmer, Icon, Loader, Pagination, PaginationProps, Segment, Table } from "semantic-ui-react";
import { RecipeBackend, RecipeDTO, RecipeListDTO } from "../../dal/RecipeBackend";
import { Redirect } from "react-router";
import { MultiResultHolder, SortState } from "../../dal/HateoasDTL";
import ConfirmationButton from "../kitchen-device/confirmationButton";

export default class ListRecipes extends Component<Properties, State> {

    private static readonly EMPTY_STATE = {
        page: 0,
        redirectPath: '',
        sort: new SortState(),
        selected: -1,
        elem: Element
    };
    navigateEdit = () => this.setState({ redirectPath: `/recipe/add/${!!this.state.recipes ? this.state.recipes._embedded.recipes[ this.state.selected ].id : -1}` });
    navigateAdd = () => this.setState({ redirectPath: `/recipe/add` });
    requestDeletion = () => {
        if (this.state.selected == -1) {
        } else {
            this.props.backend.deleteDtoById(!!this.state.recipes ? this.state.recipes._embedded.recipes[ this.state.selected ].id : -1)
                .then(() => this.props.backend.pageDto(this.state.page, this.state.sort))
                .then(value => this.setState({ recipes: value }));
        }
    };
    pageChange = (event: any, data: PaginationProps) =>
        this.props.backend.pageDto(data.activePage as number, this.state.sort)
            .then(value => this.setState({ recipes: value, page: data.activePage as number }));
    tableSort = (clickedColumn: string) => () => {
        this.setState(() => {
            // Do not reverse, imperative programming
            this.state.sort.shouldReverseDirection(this.state.sort.column == clickedColumn);
            this.state.sort.column = clickedColumn;
            return this.state;
        });
        this.props.backend.pageDto(this.state.page, this.state.sort)
            .then(value => this.setState({ recipes: value }));
    };
    rowSelection = (selectedIndex: number) => () => {
        this.setState({ selected: this.state.selected != selectedIndex ? selectedIndex : -1 });
    };
    renderItem = (recipe: RecipeDTO, index: number) =>
        <Table.Row key={recipe.id} active={this.state.selected == index}>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{recipe.id}</a>
            </Table.Cell>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{recipe.name}</a>
            </Table.Cell>
        </Table.Row>;
    renderHeader = () =>
        <Table.Row>
            <Table.HeaderCell sorted={this.state.sort.column == 'id' ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort('id')}>Id</Table.HeaderCell>
            <Table.HeaderCell sorted={this.state.sort.column == 'name' ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort('name')}>Name</Table.HeaderCell>
        </Table.Row>;
    renderPagination = () =>
        <Table.Row>
            <Table.HeaderCell colSpan='2'>
                <Pagination
                    totalPages={!!this.state.recipes ? this.state.recipes.page.totalPages : 0}
                    activePage={!!this.state.recipes ? this.state.recipes.page.number : 0}
                    ellipsisItem={{ content: <Icon name='ellipsis horizontal'/>, icon: true }}
                    firstItem={null}
                    lastItem={null}
                    prevItem={{ content: <Icon name='angle left'/>, icon: true }}
                    nextItem={{ content: <Icon name='angle right'/>, icon: true }}
                    onPageChange={this.pageChange}
                />
            </Table.HeaderCell>
        </Table.Row>;
    renderButtons = () =>
        <div>
            <Button primary onClick={this.navigateAdd}>Add</Button>
            <Button secondary onClick={this.navigateEdit}>Edit</Button>
            <ConfirmationButton openerContent='Delete Item'
                                onClick={this.requestDeletion}
                                popupButtonColor='red'
                                popupButtonContent='Confirm'/>
        </div>;
    renderTable = () =>
        <Segment>
            {this.renderRedirect()}
            {this.renderButtons()}
            <Table celled sortable structured fixed>
                <Table.Header>
                    {this.renderHeader()}
                </Table.Header>
                <Table.Body>
                    {this.state.recipes != undefined && this.state.recipes._embedded.recipes
                        .map((value, index) => this.renderItem(value, index))}
                </Table.Body>
                <Table.Footer>
                    {this.renderPagination()}
                </Table.Footer>
            </Table>
        </Segment>;
    renderIndicator = () =>
        <Segment>
            <Dimmer active>
                <Loader/>
            </Dimmer>
        </Segment>;

    constructor(properties: Properties) {
        super(properties);
        this.state = ListRecipes.EMPTY_STATE;
        this.props.backend.defaultPageDto().then(value => this.setState({ recipes: value }));
    }

    renderRedirect(): React.ReactNode {
        if (this.state.redirectPath != '') {
            return (<Redirect to={this.state.redirectPath}/>);
        }
    }

    render() {
        return this.state.recipes != undefined ? this.renderTable() : this.renderIndicator();
    }
}

interface Properties {
    backend: RecipeBackend;
}

interface State {
    recipes?: MultiResultHolder<RecipeListDTO>;
    redirectPath: string;
    page: number;
    sort: SortState;
    selected: number;
}
