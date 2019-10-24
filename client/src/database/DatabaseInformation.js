import React, {Component} from 'react';
import {Button, Card} from 'antd';
import {withRouter} from 'react-router-dom';
import './DatabaseInformation.css';

class DatabaseInformation extends Component {
    constructor(props) {
        super(props);
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
                        <Button type="primary">Start Scan</Button>
                    </Card>
                </div>
            );
        } else {
            return (<div/>);
        }
    }
}

export default withRouter(DatabaseInformation);