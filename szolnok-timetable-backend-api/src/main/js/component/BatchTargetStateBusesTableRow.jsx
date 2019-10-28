'use strict';

const React = require('react');
const Button = require('react-bootstrap').Button;

class BatchTargetStateBusesTableRow extends React.Component {

    constructor(props) {
        super(props);

        this.remove = this.remove.bind(this);
    }

    remove(busName) {
        alert(busName)
    }

    render() {
        return (
            <tr key={this.props.bus.busName}>
                <td># {this.props.bus.batchJobId}</td>
                <td>{this.props.bus.busName}</td>
                <td>{this.props.bus.startBusStop}</td>
                <td>{this.props.bus.endBusStop}</td>
                <td>
                    <Button variant="danger" type="submit" onClick={() => this.remove(this.props.bus.busName)}>
                        Remove
                    </Button>
                </td>
            </tr>
        )
    }

}

module.exports = BatchTargetStateBusesTableRow;