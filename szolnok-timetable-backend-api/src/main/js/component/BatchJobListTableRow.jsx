'use strict';

const React = require('react');

class BatchJobListTableRow extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let rowClass = "";
        if (!this.props.job.finished) {
            rowClass = "bg-success progress-bar-striped"
        } else if (this.props.job.finished && this.props.job.status !== 'COMPLETED') {
            rowClass = "bg-danger progress-bar-striped"
        }
        return (
            <tr key={this.props.job.id}
                className={rowClass}>
                <td>{this.props.job.id}</td>
                <td>{this.props.job.type}</td>
                <td>{this.props.job.parameters}</td>
                <td>{this.props.job.startTime}</td>
                <td>{this.props.job.finishTime}</td>
                <td>{this.props.job.finished ? "YES" : "NO"}</td>
                <td>{this.props.job.status}</td>
            </tr>
        );
    }
}

module.exports = BatchJobListTableRow;