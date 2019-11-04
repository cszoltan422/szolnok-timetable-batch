'use strict';

const React = require('react');
const Table = require("react-bootstrap").Table;
const Spinner = require("react-bootstrap").Spinner;
const Badge = require("react-bootstrap").Badge;
const BatchJobListTableHeader = require("./BatchJobListTableHeader");
const BatchJobListTableRow = require("./BatchJobListTableRow");
const LiveRefreshSwitch = require("./LiveRefreshSwitch");
const client = require('../client');

class BatchJobListTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            jobList: [],
            fetched: false
        };

        this.fetchJobs = this.fetchJobs.bind(this);
    }

    componentDidMount() {
        this.fetchJobs();
    }

    fetchJobs() {
        client({
            method: 'GET',
            path: '/admin/api/jobs/recent',
            headers: {'Accept': 'application/json'}
        }).done(response => {
            this.setState({
                jobList: response.entity,
                fetched: true
            })
        });
    }

    render() {
        let toRender;
        if (this.state.fetched) {
            toRender =
                <div className="batch-jobs-table">
                    <div className="batch-jobs-table-content">
                        <h2 className="batch-jobs-table-content-title">
                            <Badge variant="secondary">Recent Jobs</Badge>
                        </h2>
                        <LiveRefreshSwitch
                            componentId="live-reload-jobs-switch"
                            callback={this.fetchJobs}/>
                        <Table striped bordered hover>
                            <BatchJobListTableHeader />
                            <tbody>
                            {this.state.jobList.map(job => (
                                <BatchJobListTableRow job={job}/>
                            ))}
                            </tbody>
                        </Table>
                    </div>
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

module.exports = BatchJobListTable;