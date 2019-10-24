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
const { Content } = Layout;

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      databaseInfo: null,
      isLoading: false
    };


    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });    
  }

  getDatabaseInfo() {
    this.setState({
      isLoading: true
    });
    DatabaseAPI.getDatabaseInfo()
    .then(response => {
      this.setState({
        databaseInfo: response,
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
              <Switch>      
                <Route exact path="/" 
                  render={(props) => <TransactionList {...props} />}>
                </Route>
              </Switch>
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
