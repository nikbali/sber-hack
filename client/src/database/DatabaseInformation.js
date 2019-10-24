import React, {Component} from 'react';
import {Button, Card} from 'antd';
import {withRouter} from 'react-router-dom';
import './DatabaseInformation.css';

class DatabaseInformation extends Component {
    constructor(props) {
        super(props);
    }


    render() {
        let url = this.props.connectionString;

        if (url) {

            return (
                <div>
                    <Card title="Database Information" style={{width: 600, marginTop: 20}}>
                        <p>URL: {url}</p>
                        <p>User:</p>
                        <p>Scanner enable:</p>
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