import React, { Component } from 'react';

class Hello extends Component {
  // constructor(){
  //   super()
  // }
  update(e){
    this.SetState({"age":"1"})
  }
  render() {
    return <div>Hello World</div>
  }
}
//const App =  () => <div>Hello World</div>

export default Hello;
