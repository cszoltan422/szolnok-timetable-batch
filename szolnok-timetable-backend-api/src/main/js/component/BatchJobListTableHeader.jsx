'use strict';

const React = require('react');

class BatchJobListTableHeader extends React.Component {

    constructor(props) {
        super(props)
    }

    render() {
        return (
            <thead>
            <tr>
                <th>#id</th>
                <th>Type</th>
                <th>Parameters</th>
                <th>Start Time</th>
                <th>Finish Time</th>
                <th>Finished</th>
                <th>Status</th>
            </tr>
            </thead>
        )
    }
}

module.exports = BatchJobListTableHeader;