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
    }

    loadTransactionList() {
            //крутим бублик
            this.setState({
                isLoading: true
            });

            DatabaseAPI.getLastTransactions().then(response => {

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

                this.openNotification('error', error.message);
            });
        }


    componentDidMount() {
        this._isMounted = true;
        this.loadTransactionList();
    }

    componentWillUnmount() {
        this._isMounted = false;
    }


    render() {

        let testData = this.state.transactions;
        debugger;
        if (testData === undefined || testData.length === 0) {

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
                        testData.map(transaction => {
                            return (
                                <Card title={'Transaction Id: ' + transaction.txId}>
                                    <p
                                        style={{
                                            fontSize: 14,
                                            color: 'rgba(0, 0, 0, 0.85)',
                                            marginBottom: 10,
                                            fontWeight: 500,
                                        }}
                                    >
                                        List instructions:
                                    </p>

                                    {
                                        transaction.instructions.map(instruction => {
                                            return (
                                                <Card type="inner"
                                                      title={instruction.ts}

                                                      style={{
                                                          marginBottom: 5,
                                                      }}
                                                >
                                                    <p>SQL: {instruction.sql}</p>
                                                    <p>Undo SQL: {instruction.undoSql}</p>
                                                </Card>
                                            );
                                        })
                                    }

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