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
      connectionString: null,
      isLoading: false
    };


    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });    
  }

  getDatabaseInfo() {
    let d;

    this.setState({
      isLoading: true
    });
    DatabaseAPI.getDatabaseInfo()
    .then(response => {
      d = response.connectionString;
      this.setState({
        connectionString:d,
        isLoading: false

      });
    }).catch(error => {
      debugger;
      this.setState({
        isLoading: false
      });  
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
                  <DatabaseInformation connectionString={this.state.connectionString}/>
                  <TransactionList />
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
