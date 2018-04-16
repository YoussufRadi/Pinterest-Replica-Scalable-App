import React, { Component } from "react";
import {
  Button,
  Radio,
  HelpBlock,
  FormGroup,
  FormControl,
  ControlLabel
} from "react-bootstrap";
//import LoaderButton from "../components/LoaderButton";
import "./Signup.css";
import { Link, withRouter } from 'react-router-dom';

class SignUp extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isLoading: false,
      email: "",
      password: "",
      firstname: "",
      username: "",
      lastname: "",
      age: null,
      gender:false,
      confirmPassword: "",
      confirmationCode: "",
      newUser: null
    };
  }

  validateForm() {
    return (
      this.state.email.length > 0 &&
      this.state.password.length > 0 &&
      this.state.password === this.state.confirmPassword
    );
  }

  validateConfirmationForm() {
    return this.state.confirmationCode.length > 0;
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  handleSubmit = async event => {
    event.preventDefault();

    this.setState({ isLoading: true });

    //this.setState({ newUser: "test" });
    this.props.history.push('/hello');

    this.setState({ isLoading: false });
  }

  handleConfirmationSubmit = async event => {
    event.preventDefault();

    this.setState({ isLoading: true });
  }

  // renderConfirmationForm() {
  //   return (
  //     <form onSubmit={this.handleConfirmationSubmit}>
  //       <FormGroup controlId="confirmationCode" bsSize="large">
  //         <ControlLabel>Confirmation Code</ControlLabel>
  //         <FormControl
  //           autoFocus
  //           type="tel"
  //           value={this.state.confirmationCode}
  //           onChange={this.handleChange}
  //         />
  //         <HelpBlock>Please check your email for the code.</HelpBlock>
  //       </FormGroup>
  //       <LoaderButton
  //         block
  //         bsSize="large"
  //         disabled={!this.validateConfirmationForm()}
  //         type="submit"
  //         isLoading={this.state.isLoading}
  //         text="Verify"
  //         loadingText="Verifying…"
  //       />
  //     </form>
  //   );
  // }

  renderForm() {
    return (
      <form onSubmit={this.handleSubmit}>

      <FormGroup controlId="firstname" bsSize="large">
        <ControlLabel>First Name</ControlLabel><br></br>
        <FormControl
          autoFocus
          type="text"
          //value={this.state.firstname}
          onChange={this.handleChange}
        />

        </FormGroup>


        <FormGroup controlId="lastname" bsSize="large">
          <ControlLabel>Last Name</ControlLabel><br></br>
          <FormControl
            autoFocus
            // type="email"
            value={this.state.lastname}
            onChange={this.handleChange}
          />
           </FormGroup>

          <FormGroup controlId="username" bsSize="large">
            <ControlLabel>Username</ControlLabel><br></br>
            <FormControl
              autoFocus
              // type="email"
              value={this.state.username}
              onChange={this.handleChange}
            />
      </FormGroup>

            <FormGroup controlId="age" bsSize="large">
              <ControlLabel>Age</ControlLabel><br></br>
              <FormControl
                autoFocus
                // type="email"
                value={this.state.age}
                onChange={this.handleChange}
              />
             </FormGroup>

        <FormGroup controlId="email" bsSize="large">
          <ControlLabel>Email</ControlLabel><br></br>
          <FormControl
            autoFocus
            type="email"
            value={this.state.email}
            onChange={this.handleChange}
          />
        </FormGroup>

        <FormGroup controlId="gender" bsSize="large">
          <ControlLabel>Gender</ControlLabel><br></br>


        <radioGroup onChange= {this.handleChange} value={this.state.gender}>
        <Radio value="true"> Female </Radio>
        <Radio value="false"> Male </Radio>
        </radioGroup>


       </FormGroup>


        <FormGroup controlId="password" bsSize="large">
          <ControlLabel>Password</ControlLabel>
          <FormControl
            value={this.state.password}
            onChange={this.handleChange}
            type="password"
          />
        </FormGroup>
        <FormGroup controlId="confirmPassword" bsSize="large">
          <ControlLabel>Confirm Password</ControlLabel>
          <FormControl
            value={this.state.confirmPassword}
            onChange={this.handleChange}
            type="password"
          />
        </FormGroup>
        <Button
          block
          bsSize="large"
          disabled={!this.validateForm()}
          type="submit"
          //isLoading={this.state.isLoading}
          text="Signup"
          //loadingText="Signing up…"
        >
        Sign Up
      </Button>
      </form>
    );
  }

  render() {
    return (
      <div className="Signup">
        {this.state.newUser === null
          ? this.renderForm()
          : this.renderConfirmationForm()}
      </div>
    );
  }
}
export default withRouter(SignUp);
