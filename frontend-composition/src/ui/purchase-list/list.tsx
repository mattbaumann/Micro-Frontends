import { Component } from "react";
import { PurchaseListBackend, PurchaseListDTOList } from "../../dal/PurchaseListBackend";
import React from "react";
import { Button, Icon, Pagination, PaginationProps, Table } from "semantic-ui-react";
import { MultiResultHolder } from "../../dal/HateoasDTL";
import { RecipeDTO } from "../../dal/RecipeBackend";
import { Redirect } from "react-router";

export default class ListPurchaseLists extends Component<Parameters, State> {

    constructor(properties: Parameters) {
        super(properties);
        this.state = {purchaseLists: {
            _embedded: {purchaseLists: []},
                page: {number:0, totalPages: 0, totalElements: 0, size: 0},
                _links: {}},
            page: 0,
            redirectPath: ''
        };
        this.props.backend.defaultPageDto().then(value => this.setState({ purchaseLists: value }));
    }

    navigateEdit = (id: number) => () => this.setState({ redirectPath: `/purchaseList/add/${id}` });

    requestDeletion = (id: number) => () =>
        this.props.backend.deleteDtoById(id)
            .then(() => this.props.backend.defaultPageDto())
            .then(value => this.setState({ purchaseLists: value }));

    pageChange = (event: any, data: PaginationProps) =>
        this.props.backend.pageDto(data.activePage as number)
            .then(value => this.setState({ purchaseLists: value }));

    renderItem = (purchaseList: RecipeDTO) =>
        <Table.Row key={purchaseList.id}>
            {this.renderRedirect()}
            <Table.Cell>
                {purchaseList.id}
            </Table.Cell>
            <Table.Cell>
                {purchaseList.name}
            </Table.Cell>
            <Table.Cell>
                {purchaseList.date}
            </Table.Cell>
            <Table.Cell>
                <Button icon='pencil' content='Edit' onClick={this.navigateEdit(purchaseList.id)}/>
                <Button icon='trash' content='Delete' onClick={this.requestDeletion(purchaseList.id)}/>
            </Table.Cell>
        </Table.Row>;

    renderRedirect(): React.ReactNode {
        if (this.state.redirectPath != '') {
            return (<Redirect to={this.state.redirectPath}/>);
        }
    }
                
    renderHeader = () =>
        <Table.Row>
            <Table.HeaderCell>Id</Table.HeaderCell>
            <Table.HeaderCell>Name</Table.HeaderCell>
            <Table.HeaderCell>Date</Table.HeaderCell>
            <Table.HeaderCell>Actions</Table.HeaderCell>
        </Table.Row>;

    renderPagination = () =>
        <Table.Row>
            <Table.HeaderCell colSpan='4'>
                <Pagination
                    totalPages={this.state.purchaseLists.page.totalPages}
                    activePage={this.state.purchaseLists.page.number}
                    ellipsisItem={{ content: <Icon name='ellipsis horizontal'/>, icon: true }}
                    firstItem={null}
                    lastItem={null}
                    prevItem={{ content: <Icon name='angle left'/>, icon: true }}
                    nextItem={{ content: <Icon name='angle right'/>, icon: true }}
                    onPageChange={this.pageChange}
                />
            </Table.HeaderCell>
        </Table.Row>;
        
    render(): React.ReactNode {
        return <Table celled>
            <Table.Header>
                {this.renderHeader()}
            </Table.Header>
            <Table.Body>
                {this.state.purchaseLists._embedded.purchaseLists.map(purchaseList => this.renderItem(purchaseList))}
            </Table.Body>
            <Table.Footer>
                {this.renderPagination()}
            </Table.Footer>
        </Table>
    }
}

    interface State {
        purchaseLists: MultiResultHolder<PurchaseListDTOList>;
        redirectPath: string;
        page: number;
    }

    interface Parameters {
        backend: PurchaseListBackend;
    }