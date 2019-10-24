import React, { Component } from 'react';

import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { withRouter } from 'react-router-dom';
import './TransactionList.css';

class TransactionList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            polls: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false
        };
        this.loadTransactionList = this.loadTransactionList.bind(this);
    }

    loadTransactionList() {

        
    }

    componentDidMount() {
        this.loadTransactionList();
    }


    handleLoadMore() {
    }



    render() {

        return (
            <div className="transaction-container">
                <div className="no-transaction-found">
                    <span>No Transactions Found.</span>
                </div>
            </div>
        );
    }
}

export default withRouter(TransactionList);