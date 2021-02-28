import React from 'react';
import './App.css';
import {FirstComponent} from "./components/FirstComponent";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <FirstComponent car={"saab"}></FirstComponent>
      </header>
    </div>
  );
}

export default App;
