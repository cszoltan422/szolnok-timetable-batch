'use strict';

const React = require('react');
const Badge = require('react-bootstrap').Badge;
const LaunchBatchJobFormContent = require('./LaunchBatchJobFormContent');

class LaunchBatchJobForm extends React.Component{

    constructor(props) {
        super(props)

    }

    render() {
        return (
            <div className="launch-batch-job">
                <div className="launch-batch-job-content">
                    <h2>
                        <Badge variant="secondary">Launch a Job</Badge>
                    </h2>
                    <LaunchBatchJobFormContent />
                </div>
            </div>
        )
    }

}

module.exports = LaunchBatchJobForm;