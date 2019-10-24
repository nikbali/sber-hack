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
            isLoading: false
        };
        this.loadTransactionList = this.loadTransactionList.bind(this);
    }

    loadTransactionList() {
            //крутим бублик
            this.setState({
                isLoading: true
            });

            DatabaseAPI.getLastTransactions().then(response => {

                const transactions = this.state.transactions.slice();

                this.setState({
                    transactions: transactions.concat(response.content),
                    isLoading: false

                });

            }).catch(error => {

                this.setState({
                    isLoading: false
                });

                debugger;
                this.openNotification('error', error.message);
            });
        }


    componentDidMount() {
        this.loadTransactionList();
    }


    render() {
        let testData = [
            {
                txId: 1,
                instructions:[
                    {
                        sql: "String",
                        undoSql: "String",
                        ts: "Instant"
                    },{
                        sql: "String",
                        undoSql: "String",
                        ts: "Instant"
                    }
                ]
            },{
                txId: 2,
                instructions:[
                    {
                        sql: "String",
                        undoSql: "String",
                        ts: "Instant"
                    },{
                        sql: "String",
                        undoSql: "String",
                        ts: "Instant"
                    }
                ]
            },{
                txId: 3,
                instructions:[
                    {
                        sql: "String",
                        undoSql: "String",
                        ts: "Instant"
                    },{
                        sql: "String",
                        undoSql: "String",
                        ts: "Instant"
                    }
                ]
            }

        ];

        let cnt = this.state.transactions.length;
        if (true) {

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

        } else {
            return (
                <div className="transaction-container">
                    <div className="no-transaction-found">
                        <span>No Transactions Found.</span>
                    </div>
                </div>
            );
        }

    }
}

export default withRouter(TransactionList);