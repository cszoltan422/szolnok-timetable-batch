'use strict';

const React = require('react');
const client = require('../client');
const Form = require("react-bootstrap").Form;

class LiveRefreshSwitch extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            reload: {
                enabled: false,
                current: null
            }
        };

        this.onSwitchChange = this.onSwitchChange.bind(this);
    }

    defaultCallback() {
        alert("Default callback! You can provide your own with setting the callback property!");
    }

    onSwitchChange() {
        if (this.state.reload.enabled) {
            clearInterval(this.state.reload.current);
            this.setState({
                reload: {
                    enabled: false,
                    current: null
                }
            });
        } else {
            const callback = this.props.callback || this.defaultCallback;
            const timeout = this.props.timeout || 3000;
            const interval = setInterval(() => {
                callback();
            }, timeout);
            this.setState({
                reload: {
                    enabled: true,
                    current: interval
                }
            });
        }
    }

    render() {
        const componentId = this.props.componentId;
        return (
            <Form className="mb-3">
                <Form.Check
                    className="switch-form"
                    type="switch"
                    id={componentId}
                    label="Enable automatic refresh"
                    onChange={() => this.onSwitchChange()}
                />
            </Form>
        )
    }
}

module.exports = LiveRefreshSwitch;