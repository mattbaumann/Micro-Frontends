import { Component } from "react";
import * as React from "react";
import { Button, Icon, Pagination, PaginationProps, Segment, Table, Dimmer, Loader } from "semantic-ui-react";
import { HateoasDTL, HateoasDTO, MultiResultHolder, SortState } from "../../dal/HateoasDTL";
import { Redirect } from "react-router";
import { KitchenDeviceDTO } from "../../dal/KitchenDevice";
import ConfirmationButton from "./confirmationButton";

export default class KitchenDeviceLists<T extends HateoasDTO, L> extends Component<Parameters<T, L>, State<L>> {

    private static readonly EMPTY_STATE = {
        page: 0,
        redirectPath: '',
        sort: new SortState(),
        selected: -1,
        elem: Element
    };

    constructor(properties: Parameters<T, L>) {
        super(properties);
        this.state = KitchenDeviceLists.EMPTY_STATE;
        this.props.backend.defaultPageDto().then(value => this.setState({ items: value }));
    }

    navigateEdit = () => this.setState({ redirectPath: `/kitchenDevices/add/${this.state.selected}` });
    navigateAdd = () => this.setState({ redirectPath: `/kitchenDevices/add` });

    requestDeletion = () => {
        if (this.state.selected == -1) {
        } else {
            this.props.backend.deleteDtoById(this.state.selected)
                .then(() => this.props.backend.pageDto(this.state.page, this.state.sort))
                .then(value => this.setState({ items: value }));
        }
    };

    pageChange = (event: any, data: PaginationProps) =>
        this.props.backend.pageDto(data.activePage as number, this.state.sort)
            .then(value => this.setState({ items: value, page: data.activePage as number }));

    tableSort = (clickedColumn: string) => () => {
        this.setState(() => {
            // Do not reverse, imperative programming
            this.state.sort.shouldReverseDirection(this.state.sort.column == clickedColumn);
            this.state.sort.column = clickedColumn;
        });
        this.props.backend.pageDto(this.state.page, this.state.sort)
            .then(value => this.setState({ items: value }));
    };

    rowSelection = (selectedIndex: number) => () => {
        this.setState({ selected: this.state.selected != selectedIndex ? selectedIndex : -1 });
    };

    renderItem = (kitchenDevice: KitchenDeviceDTO, index: number) =>
        <Table.Row key={kitchenDevice.id}
                   positive={kitchenDevice.available}
                   negative={!kitchenDevice.available}
                   active={this.state.selected == index}>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{kitchenDevice.id}</a>
            </Table.Cell>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{kitchenDevice.name}</a>
            </Table.Cell>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>{kitchenDevice.function}</a>
            </Table.Cell>
            <Table.Cell>
                <a onClick={this.rowSelection(index)}>
                    <Icon color={kitchenDevice.available ? 'green' : 'red'}
                          name={kitchenDevice.available ? 'check' : 'times'}/>
                </a>
            </Table.Cell>
        </Table.Row>;

    renderRedirect(): React.ReactNode {
        if (this.state.redirectPath != '') {
            return (<Redirect to={this.state.redirectPath}/>);
        }
    }

    renderHeader = () => {
        return this.props.headers.map(value => (
            <Table.HeaderCell key={value}
                              sorted={this.state.sort.column == value ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort(value)}>
                {value}
            </Table.HeaderCell>
        ));
    };

    renderPagination = () =>
        <Table.Row>
            <Table.HeaderCell colSpan='5'>
                <Pagination
                    totalPages={!!this.state.items ? this.state.items.page.totalPages : 0}
                    activePage={!!this.state.items ? this.state.items.page.number : 0}
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
            {this.renderRedirect()};
            {this.renderButtons()};
            <Table celled sortable structured fixed>
                <Table.Header>
                    <Table.Row>
                        {this.renderHeader()}
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {this.props.rendering(this.state.items as MultiResultHolder<L>)}
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

    render(): React.ReactNode {
        return this.state.items != undefined ? this.renderTable() : this.renderIndicator();
    }
}

interface State<L> {
    items?: MultiResultHolder<L>;
    redirectPath: string;
    page: number;
    sort: SortState;
    selected: number;
}

interface Parameters<T extends HateoasDTO, L> {
    backend: HateoasDTL<T, L>;
    headers: string[];
    rendering: (results: MultiResultHolder<L>) => React.ReactNode;
}