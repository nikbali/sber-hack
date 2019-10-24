import React, { Component } from 'react';
import {
    Link
} from 'react-router-dom';
import './AppHeader.css';
import { Layout, Menu, Dropdown, Icon } from 'antd';
const Header = Layout.Header;
    
class AppHeader extends Component {
    constructor(props) {
        super(props);
    }


    render() {

        return (
            <Header className="app-header">
            <div className="container">
              <div className="app-title" >
                <Link to="/">Database Scanner</Link>
              </div>
            </div>
          </Header>
        );
    }
}


export default AppHeader;