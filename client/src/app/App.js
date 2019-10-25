import React, { Component } from 'react';
import './App.css';
import {
  Route,
  withRouter,
  Switch
} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import LoadingIndicator from '../common/LoadingIndicator';
import { Layout, notification } from 'antd';
import {DatabaseAPI} from "../api/api";
import TransactionList from "../transaction/TransactionList";
import DatabaseInformation from "../database/DatabaseInformation";
const { Content } = Layout;

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      serverActive: false,
      connectionString: null,
      username: null,
      scanActive: false,
      isLoading: false
    };

  }

  openNotification = (type, text) => {
    notification[type]({
      message: 'Error',
      description: text
    });
  };

  async getDatabaseInfo() {
    this.setState({
      isLoading: true
    });

    DatabaseAPI.getDatabaseInfo().then(response => {

      this.setState({
        connectionString: response.connectionString,
        username: response.username,
        scanActive: response.scanActive,
        isLoading: false,
        serverActive: true
      });
    }).catch(error => {

      this.setState({
        isLoading: false
      });
      this.openNotification('error', error.message);
    });
  }

  componentDidMount() {
    this.getDatabaseInfo();
  }

  render() {
    if(this.state.isLoading) {
      return <LoadingIndicator />
    }

    return (
        <Layout className="app-container">
          <AppHeader />
          <Content className="app-content">
            <div className="container">
                  <DatabaseInformation
                      connectionString={this.state.connectionString}
                      username = {this.state.username}
                      scanActive = {this.state.scanActive}
                  />
                  <TransactionList
                      serverActive = {this.state.serverActive}
                  />
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
