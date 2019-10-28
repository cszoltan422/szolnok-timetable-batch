'use strict';

const BatchJobListTable = require("./BatchJobListTable");
const LaunchBatchJobForm = require("./LaunchBatchJobForm");
const BatchTargetStateBusesTable = require("./BatchTargetStateBusesTable");
const React = require('react');

class App extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BatchJobListTable />
                <LaunchBatchJobForm />
                <BatchTargetStateBusesTable />
            </div>
        )
    }
}

module.exports = App;