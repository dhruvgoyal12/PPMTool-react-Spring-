import "./App.css";
import Dashboard from "./components/Dashboard";
import AddProject from "./components/Project/AddProject";
import "bootstrap/dist/css/bootstrap.min.css";
import { Routes, Route } from "react-router-dom";
import Header from "./components/Layout/Header";
import { Fragment } from "react";
import UpdateProject from "./components/Project/UpdateProject";

function App() {
  return (
    <div className="App">
      <Header />
      <Route exact path="/addProject" component={AddProject} />

      <Route exact path="/dashboard">
        <Dashboard />
      </Route>

      <Route exact path="/updateProject/:id" component={UpdateProject} />
    </div>
  );
}

export default App;
