import React, {Component} from 'react';
import {Button, Card, notification} from 'antd';
import {withRouter} from 'react-router-dom';
import './DatabaseInformation.css';
import {DatabaseAPI} from "../api/api";

class DatabaseInformation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            scanActive: this.props.scanActive,
        };

        this.startClick = this.startClick.bind(this);
        this.openNotification = this.openNotification.bind(this);
        this.endClick = this.endClick.bind(this);
    }

    openNotification = (type, text) => {
        notification[type]({
            message: 'Info',
            description: text
        });}


    startClick(event) {

        DatabaseAPI.start().then(response => {

            this.setState({
                scanActive: true,
            });

            this.openNotification('error', 'Сканнирование запущено');
        }).catch(error => {

            this.openNotification('error', error.message);
        });

    }

    endClick(event) {

        DatabaseAPI.end().then(response => {

            this.setState({
                scanActive: false,
            });

            this.openNotification('error', 'Сканнирование остановлено');
        }).catch(error => {
            this.openNotification('error', error.message);
        });

    }

    render() {
        let connectionString = this.props.connectionString;
        let username = this.props.username;
        let scanActive = this.props.scanActive;

        if (connectionString) {
            debugger;
            return (
                <div>
                    <Card title="Database Information" style={{width: 600, marginTop: 20}}>
                        <p>URL: {connectionString}</p>
                        <p>User: {username}</p>
                        <p>Scanner enable: {scanActive ? 'On' : 'Off'}</p>
                        <Button onClick={this.startClick}  style={{marginRight: 20}} type="primary">Start Scan</Button>
                        <Button onClick={this.endClick}  type="danger">Stop Scan</Button>
                    </Card>
                </div>
            );
        } else {
            return (<div/>);
        }
    }
}

export default withRouter(DatabaseInformation);