import React, { Component } from "react";
import { PurchaseListBackend, PurchaseListDTOList } from "../../dal/PurchaseListBackend";
import { Button, Dimmer, Icon, Loader, Pagination, PaginationProps, Segment, Table } from "semantic-ui-react";
import { MultiResultHolder, SortState } from "../../dal/HateoasDTL";
import { RecipeDTO } from "../../dal/RecipeBackend";
import { Redirect } from "react-router";
import ConfirmationButton from "../kitchen-device/confirmationButton";

export default class ListPurchaseLists extends Component<Parameters, State> {

    private static readonly EMPTY_STATE = {
        page: 0,
        redirectPath: '',
        sort: new SortState(),
        selected: -1,
        elem: Element
    };
    navigateEdit = () => this.setState({ redirectPath: `/purchaseList/add/${!!this.state.purchaseLists ? this.state.purchaseLists._embedded.purchaseLists[ this.state.selected ].id : -1}` });
    navigateAdd = () => this.setState({ redirectPath: `/kitchenDevices/add` });
    requestDeletion = () => {
        if (this.state.selected == -1) {
        } else {
            this.props.backend.deleteDtoById(!!this.state.purchaseLists ? this.state.purchaseLists._embedded.purchaseLists[ this.state.selected ].id : -1)
                .then(() => this.props.backend.pageDto(this.state.page, this.state.sort))
                .then(value => this.setState({ purchaseLists: value }));
        }
    };
    pageChange = (event: any, data: PaginationProps) =>
        this.props.backend.pageDto(data.activePage as number, this.state.sort)
            .then(value => this.setState({ purchaseLists: value, page: data.activePage as number }));
    tableSort = (clickedColumn: string) => () => {
        this.setState(() => {
            // Do not reverse, imperative programming
            this.state.sort.shouldReverseDirection(this.state.sort.column == clickedColumn);
            this.state.sort.column = clickedColumn;
            return this.state;
        });
        this.props.backend.pageDto(this.state.page, this.state.sort)
            .then(value => this.setState({ purchaseLists: value }));
    };
    rowSelection = (selectedIndex: number) => () => {
        this.setState({ selected: this.state.selected != selectedIndex ? selectedIndex : -1 });
    };
    renderItem = (purchaseList: RecipeDTO, index: number) =>
        <Table.Row key={purchaseList.id} active={this.state.selected == index}>
            {this.renderRedirect()}
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{purchaseList.id}</a>
            </Table.Cell>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{purchaseList.name}</a>
            </Table.Cell>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{purchaseList.date}</a>
            </Table.Cell>
        </Table.Row>;
    renderHeader = () =>
        <Table.Row>
            <Table.HeaderCell sorted={this.state.sort.column == 'id' ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort('id')}>Id</Table.HeaderCell>
            <Table.HeaderCell sorted={this.state.sort.column == 'name' ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort('name')}>Name</Table.HeaderCell>
            <Table.HeaderCell sorted={this.state.sort.column == 'date' ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort('date')}>Date</Table.HeaderCell>
        </Table.Row>;
    renderPagination = () =>
        <Table.Row>
            <Table.HeaderCell colSpan='5'>
                <Pagination
                    totalPages={!!this.state.purchaseLists ? this.state.purchaseLists.page.totalPages : 0}
                    activePage={!!this.state.purchaseLists ? this.state.purchaseLists.page.number : 0}
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
                    {this.state.purchaseLists != undefined && this.state.purchaseLists._embedded.purchaseLists
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

    constructor(properties: Parameters) {
        super(properties);
        this.state = ListPurchaseLists.EMPTY_STATE;
        this.props.backend.defaultPageDto().then(value => this.setState({ purchaseLists: value }));
    }

    renderRedirect(): React.ReactNode {
        if (this.state.redirectPath != '') {
            return (<Redirect to={this.state.redirectPath}/>);
        }
    }

    render(): React.ReactNode {
        return this.state.purchaseLists != undefined ? this.renderTable() : this.renderIndicator();
    }
}

interface State {
    purchaseLists?: MultiResultHolder<PurchaseListDTOList>;
    redirectPath: string;
    page: number;
    sort: SortState;
    selected: number;
}

interface Parameters {
    backend: PurchaseListBackend;
}