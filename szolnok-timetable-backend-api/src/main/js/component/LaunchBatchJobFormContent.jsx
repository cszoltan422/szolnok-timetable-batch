'use strict';

const React = require('react');
const Form = require('react-bootstrap').Form;
const Alert = require('react-bootstrap').Alert;
const Button = require('react-bootstrap').Button;
const client = require('../client');

const successAlertMessage = 'The job was successfully launched!';

class LaunchBatchJobFormContent extends React.Component{

    constructor(props) {
        super(props);

        this.state = {
            showAlert: false,
            alertType: "success",
            alertMessage: ""
        };

        this.launchJob = this.launchJob.bind(this);
        this.setShow = this.setShow.bind(this);
    }

    launchJob(event) {
        const form = event.currentTarget;
        event.preventDefault();
        event.stopPropagation();
        const jobType = form.elements.namedItem('launchJob.jobType').value;
        const jobparameters = form.elements.namedItem('launchJob.jobparameters').value;

        client({
            method: 'POST',
            path: '/admin/api/jobs/launch',
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            entity: {
                jobType: jobType,
                parameters: jobparameters
            }
        }).done(response => {
            this.setState({
                showAlert: true,
                alertType: response.entity.success ? "success" : "warning",
                alertMessage: response.entity.success ? successAlertMessage  : response.entity.message
            })
        });

    }

    render() {
        const alertText = this.state.alertMessage;
        return (
            <div>
                <Alert onClose={() => this.setShow(false)} show={this.state.showAlert} variant={this.state.alertType} dismissible>
                        {alertText}
                </Alert>
                <Form onSubmit={this.launchJob}>
                    <Form.Group controlId="launchJob.jobType">
                        <Form.Label>Batch Job Type</Form.Label>
                        <Form.Control as="select">
                            <option>szolnokBusesJob</option>
                            <option>busStopWithBusesJob</option>
                        </Form.Control>
                        <Form.Text className="text-muted">
                            Type of batch job to launch
                        </Form.Text>
                    </Form.Group>
                    <Form.Group controlId="launchJob.jobparameters">
                        <Form.Label>Batch Job parameters</Form.Label>
                        <Form.Control type="text" placeholder="Enter job parameters"/>
                        <Form.Text className="text-muted">
                            Comma separated list of buses (eg. "1A,24,34Y")
                        </Form.Text>
                    </Form.Group>
                    <Button variant="primary" type="submit" size="lg">
                        Launch
                    </Button>
                </Form>
            </div>
        )
    }

    setShow(boolean) {
        this.setState({
            showAlert: boolean
        })
    }

}

module.exports = LaunchBatchJobFormContent;