'use strict';

const Spinner = require("react-bootstrap").Spinner;
const BatchJobListTable = require("./BatchJobListTable");
const LaunchBatchJobForm = require("./LaunchBatchJobForm");
const React = require('react');
const client = require('../client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            recentJobs: [],
            fetched: false
        }
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: '/admin/api/jobs/recent',
            headers: {'Accept': 'application/json'}
        }).done(response => {
            this.setState({
                recentJobs: response.entity,
                fetched: true
            })
        });
    }

    render() {
        let toRender;
        if (this.state.fetched) {
            toRender =
                <div>
                    <BatchJobListTable jobsList={this.state.recentJobs}/>
                    <LaunchBatchJobForm />
                </div>
        } else {
            toRender =
                <div className="main-load-spinner">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </div>
        }
        return toRender
    }
}

module.exports = App;