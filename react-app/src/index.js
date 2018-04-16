import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import Hello from './Hello';
import { Route,BrowserRouter } from 'react-router-dom'
import Signup from './SignUp';

const Index = () => (

    <BrowserRouter>
      <div>
        <Route path="/" exact component={App} />
        <Route path="/hello" exact component={Hello} />
        <Route path="/Signup" exact component={Signup} />
      </div>

    </BrowserRouter>
)
//
ReactDOM.render(<Index />, document.getElementById('root'));
