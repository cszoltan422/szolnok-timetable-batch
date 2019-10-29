'use strict';

const React = require('react');
const Button = require('react-bootstrap').Button;
const client = require('../client');

class BatchTargetStateBusesTableRow extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            aboutToRemove: false
        };

        this.remove = this.remove.bind(this);
    }

    remove(busName) {
        this.setState({aboutToRemove: true});

        client({
            method: 'POST',
            path: '/admin/api/bus/' + busName + '/remove',
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'}
        }).done(response => {
            this.setState({aboutToRemove: false});
        });
    }

    render() {
        const className = this.state.aboutToRemove ? "bg-warning progress-bar-striped" : "";
        return (
            <tr className={className} key={this.props.bus.busName}>
                <td># {this.props.bus.batchJobId}</td>
                <td>{this.props.bus.busName}</td>
                <td>{this.props.bus.startBusStop}</td>
                <td>{this.props.bus.endBusStop}</td>
                <td>
                    <Button variant="danger" type="submit" onClick={() => this.remove(this.props.bus.busName)}
                            disabled={this.state.aboutToRemove}>
                        Remove
                    </Button>
                </td>
            </tr>
        )
    }

}

module.exports = BatchTargetStateBusesTableRow;