import React, { Component } from 'react';
import './App.css';

import AppHeader from './common/AppHeader';
import LoadingIndicator from './common/LoadingIndicator';

import { Layout, notification } from 'antd';
import {DatabaseAPI} from "../api/api";
const { Content } = Layout;

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      currentDatabase: null,
      isLoading: false
    };

    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });    
  }

  loadDatabaseInfo() {
    this.setState({
      isLoading: true
    });

    DatabaseAPI.getDatabaseInfo()
    .then(response => {

      this.setState({
        currentDatabase: response,
        isLoading: false
      });

    }).catch(error => {
      this.setState({
        isLoading: false
      });  
    });
  }

  componentDidMount() {
    this.loadDatabaseInfo();
  }

  render() {

    if(this.state.isLoading) {
      return <LoadingIndicator />
    }

    return (
        <Layout className="app-container">

          <AppHeader/>
          <Content className="app-content">
            <div className="container">

              {"Hello World!"}
              {/*<Panel></Panel>*/}
              {/*<ListTransactions></ListTransactions>*/}

            </div>
          </Content>
        </Layout>
    );
  }
}

export default App;
