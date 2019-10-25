'use strict';

const BatchJobListTable = require("./BatchJobListTable");
const LaunchBatchJobForm = require("./LaunchBatchJobForm");
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
            </div>
        )
    }
}

module.exports = App;