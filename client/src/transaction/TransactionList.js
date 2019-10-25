import React, { Component } from 'react';

import {Button, Card, Icon, notification} from 'antd';
import { withRouter } from 'react-router-dom';
import './TransactionList.css';
import {DatabaseAPI} from "../api/api";

class TransactionList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            transactions: [],
            isLoading: false,
        };
        this._isMounted = false;

        this.loadOperationList = this.loadOperationList.bind(this);
    }

    loadOperationList() {
            //крутим бублик
            this.setState({
                isLoading: true
            });

            DatabaseAPI.getLastOperations().then(response => {

                if(this._isMounted){

                    this.setState({
                        transactions: response,
                        isLoading: false
                    });
                }

            }).catch(error => {

                this.setState({
                    isLoading: false
                });

                //this.openNotification('error', error.message);
            });
        }


    componentDidMount() {
        this._isMounted = true;
        this.loadOperationList();
    }

    componentWillUnmount() {
        this._isMounted = false;
    }


    render() {

        let data = this.state.transactions;

        debugger;
        if (data === undefined || data.length === 0) {

            return (
                <div className="transaction-container">
                    <div className="no-transaction-found">
                        <span>No Transactions Found.</span>
                    </div>
                </div>
            );

        } else {
            return (
                <div className="transaction-container">

                    {
                        data.map(transaction => {
                            return (
                                <Card type="inner"
                                      title={'Id транзакции: ' + transaction.xid}
                                      style={{marginBottom: 5,}}>

                                    <p>Start Timestamp: {transaction.startTimestamp}</p>
                                    <p>SQL: {transaction.sqlRedo}</p>
                                    <p>Undo SQL: {transaction.sql_Undo}</p>
                                    <p>Info: {transaction.info}</p>
                                </Card>
                            )
                        })
                    }

                </div>
            );
        }

    }
}

export default withRouter(TransactionList);