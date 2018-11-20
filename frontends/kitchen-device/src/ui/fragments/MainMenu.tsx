import React, { Component } from "react";
import { Dropdown, Menu } from 'semantic-ui-react';
import { Link } from "react-router-dom";

export default class MainMenu extends Component {

    handleLink = (href: string) => () => window.location.href = href;

    render(): React.ReactNode {
        return (
            <Menu>
                <Menu.Item>
                    <Dropdown item text='Backend Integration'>
                        <Dropdown.Menu>
                            <Dropdown.Item text='List PurchaseList'
                                           onClick={this.handleLink('http://localhost:9601/backend/purchaseList/list')}/>
                            <Dropdown.Item text='Add PurchaseList'
                                           onClick={this.handleLink('http://localhost:9601/backend/purchaseList/edit')}/>
                            <Dropdown.Item text='List KitchenDevice'
                                           onClick={this.handleLink('http://localhost:9601/backend/kitchenDevice/list')}/>
                            <Dropdown.Item text='Add KitchenDevice'
                                           onClick={this.handleLink('http://localhost:9601/backend/kitchenDevice/list')}/>
                        </Dropdown.Menu>
                    </Dropdown>
                </Menu.Item>
                <Menu.Item>
                    <Dropdown item text='Portal Integration'>
                        <Dropdown.Menu>
                            <Dropdown.Item text='List PurchaseList'
                                           onClick={this.handleLink('http://localhost:9601/controller/purchaseList/list')}/>
                            <Dropdown.Item text='Add PurchaseList'
                                           onClick={this.handleLink('http://localhost:9601/controller/purchaseList/edit')}/>
                            <Dropdown.Item text='List KitchenDevice'
                                           onClick={this.handleLink('http://localhost:9601/controller/kitchenDevice/list')}/>
                            <Dropdown.Item text='Add KitchenDevice'
                                           onClick={this.handleLink('http://localhost:9601/controller/kitchenDevice/list')}/>
                        </Dropdown.Menu>
                    </Dropdown>
                </Menu.Item>
                <Menu.Item>
                    <Dropdown item text='Frontend Integration'>
                        <Dropdown.Menu>
                            <Dropdown.Item as={Link} to='/kitchenDevice/add' text='Add Kitchen Device'/>
                            <Dropdown.Item as={Link} to='/kitchenDevice/list' text='List Kitchen Devices'/>
                            <Dropdown.Divider/>
                        </Dropdown.Menu>
                    </Dropdown>
                </Menu.Item>
            </Menu>
        );
    }
}