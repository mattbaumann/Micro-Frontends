import * as React from "react";
import { Component } from "react";
import { Button, Dimmer, Icon, Loader, Pagination, PaginationProps, Segment, Table } from "semantic-ui-react";
import { HateoasDTL, HateoasDTO, MultiResultHolder, SortState } from "../../dal/HateoasDTL";
import { Redirect } from "react-router";
import { KitchenDeviceDTO, KitchenDeviceDTOList } from "../../dal/KitchenDevice";
import ConfirmationButton from "./confirmationButton";

export default class KitchenDeviceLists extends Component<Parameters<KitchenDeviceDTO, KitchenDeviceDTOList>, State<KitchenDeviceDTOList>> {

    private static readonly EMPTY_STATE = {
        page: 0,
        redirectPath: '',
        sort: new SortState(),
        selected: -1,
        elem: Element
    };

    private static readonly HEADERS = [ 'ID', 'Name', 'Function', 'available' ];
    navigateEdit = () => this.setState({ redirectPath: `/kitchenDevices/add/${!!this.state.items ? this.state.items._embedded.kitchenDevices[ this.state.selected ].id : -1}` });
    requestDeletion = () => {
        if (this.state.selected == -1) {
        } else {
            this.props.backend.deleteDtoById(!!this.state.items ? this.state.items._embedded.kitchenDevices[ this.state.selected ].id : -1)
                .then(() => this.props.backend.pageDto(this.state.page, this.state.sort))
                .then(value => this.setState({ items: value }));
        }
    };
    navigateAdd = () => this.setState({ redirectPath: `/kitchenDevices/add` });
    tableSort = (clickedColumn: string) => () => {
        this.setState(() => {
            // Do not reverse, imperative programming
            this.state.sort.shouldReverseDirection(this.state.sort.column == clickedColumn);
            this.state.sort.column = clickedColumn;
            return this.state;
        });
        this.props.backend.pageDto(this.state.page, this.state.sort)
            .then(value => this.setState({ items: value }));
    };

    pageChange = (event: any, data: PaginationProps) =>
        this.props.backend.pageDto(data.activePage as number, this.state.sort)
            .then(value => this.setState({ items: value, page: data.activePage as number }));
    renderHeader = () => {
        return KitchenDeviceLists.HEADERS.map(value => (
            <Table.HeaderCell key={value}
                              sorted={this.state.sort.column == value ? this.state.sort.getLongDirection() : undefined}
                              onClick={this.tableSort(value)}>
                {value}
            </Table.HeaderCell>
        ));
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

    renderTable = () =>
        <Segment>
            {this.renderRedirect()}
            {this.renderButtons()}
            <Table celled sortable structured fixed>
                <Table.Header>
                    <Table.Row>
                        {this.renderHeader()}
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {this.state.items != undefined && this.state.items._embedded.kitchenDevices
                        .map((value, index) => this.renderItem(value, index))}
                </Table.Body>
                <Table.Footer>
                    {this.renderPagination()}
                </Table.Footer>
            </Table>
        </Segment>;

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

    constructor(properties: Parameters<KitchenDeviceDTO, KitchenDeviceDTOList>) {
        super(properties);
        this.state = KitchenDeviceLists.EMPTY_STATE;
        this.props.backend.defaultPageDto().then(value => this.setState({ items: value }));
    }

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
}