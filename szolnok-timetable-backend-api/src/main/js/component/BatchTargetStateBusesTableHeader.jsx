'use strict';

const React = require('react');

class BatchTargetStateBusesTableHeader extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <thead>
            <tr>
                <th># Batch Job Id</th>
                <th># Bus Name</th>
                <th>Start Stop</th>
                <th>End Stop</th>
                <th>Remove</th>
            </tr>
            </thead>
        )
    }

}

module.exports = BatchTargetStateBusesTableHeader;