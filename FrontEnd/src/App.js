import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
// class App extends Component {
//   // constructor(){
//   //   super()
//   // }
//   update(e){
//     this.SetState({"age":"1"})
//   }
//   render() {
//     return <div>Hello World</div>
//   }
// }
// const App =  () => <div>Hello World</div>

//export default App;
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import "./Login.css";


class Login extends Component {
  constructor(props) {
    super(props);

    this.state = {
      email: "",
      password: ""
    };
    this.handleSignUp.bind(this);
  }

  validateForm() {
    return this.state.email.length > 0 && this.state.password.length > 0;
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
  }

  handleSubmit = event => {
    event.preventDefault();
    this.props.history.push('/hello');
    // use fetch to get the id of the user
    // use localstorage to persist the user's id
    // route to hello.js and get the id from localstorage
  }
   handleSignUp = event => {
  this.props.history.push('/Signup');
  }

  render() {
    return (
       <div className="Login">


        <form onSubmit={this.handleSubmit}>
          <FormGroup controlId="email" bsSize="large">
            <ControlLabel>Email</ControlLabel><br></br>
            <FormControl
              autoFocus
              type="email"
              value={this.state.email}
              onChange={this.handleChange}
            />
          </FormGroup>
          <FormGroup controlId="password" bsSize="large">
            <ControlLabel>Password</ControlLabel>
            <FormControl
              value={this.state.password}
              onChange={this.handleChange}
              type="password"
            />
          </FormGroup>
          <Button id="btnLogin"
             block
             bsSize="large"
            disabled={!this.validateForm()}
            type="submit"
            background ="#FF0000"
          >
            Login
          </Button>
          <Button id="btnSignUp"
             block
             bsSize="large"

           onClick= {this.handleSignUp}
            background ="#FF0000"
          >
            Sign Up
          </Button>

        </form>

      </div>
     );
  }
}
  export default withRouter(Login);
