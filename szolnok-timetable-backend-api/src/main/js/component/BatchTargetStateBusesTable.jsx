'use strict';

const React = require('react');
const client = require('../client');
const Table = require("react-bootstrap").Table;
const Badge = require("react-bootstrap").Badge;
const Spinner = require("react-bootstrap").Spinner;
const BatchTargetStateBusesTableRow = require('./BatchTargetStateBusesTableRow');
const BatchTargetStateBusesTableHeader = require('./BatchTargetStateBusesTableHeader');

class BatchTargetStateBusesTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            buses: [],
            fetched: false
        };

        this.fetchBatchBuses = this.fetchBatchBuses.bind(this);
    }

    componentDidMount() {
        this.fetchBatchBuses();
        setInterval(() => {
            this.fetchBatchBuses();
        }, 3000);
    }

    fetchBatchBuses() {
        client({
            method: 'GET',
            path: '/admin/api/bus/',
            headers: {'Accept': 'application/json'}
        }).done(response => {
            this.setState({
                buses: response.entity,
                fetched: true
            })
        });
    }

    render() {
        let toRender;
        if (this.state.fetched) {
            toRender =
                <div className="batch-target-state-buses-table">
                    <div className="batch-target-state-buses-table-content">
                        <h2>
                            <Badge variant="secondary">Buses in Batch Target State</Badge>
                        </h2>
                        <Table striped bordered hover>
                            <BatchTargetStateBusesTableHeader />
                            <tbody>
                            {this.state.buses.map(bus => (
                                <BatchTargetStateBusesTableRow bus={bus}/>
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

module.exports = BatchTargetStateBusesTable;