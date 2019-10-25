'use strict';

const React = require('react');
const Table = require("react-bootstrap").Table;
const Badge = require("react-bootstrap").Badge;
const BatchJobListTableHeader = require("./BatchJobListTableHeader");
const BatchJobListTableRow = require("./BatchJobListTableRow");

class BatchJobListTable extends React.Component {

    constructor(props) {
        super(props)
    }

    render() {
        return (
            <div className="batch-jobs-table">
                <div className="batch-jobs-table-content">
                    <h2>
                        <Badge variant="secondary">Recent Jobs</Badge>
                    </h2>
                    <Table striped bordered hover>
                        <BatchJobListTableHeader />
                        <tbody>
                        {this.props.jobsList.map(job => (
                            <BatchJobListTableRow job={job}/>
                        ))}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }
}

module.exports = BatchJobListTable;