import * as React from "react";
import {render} from "react-dom";
import {Route} from "wouter";
import MainPage from "./pages/MainPage";
import LoginForm from "./pages/LoginForm";

const App = () => (
  <>
    <Route path="/">
      <MainPage/>
    </Route>
    <Route path="/login">
      <LoginForm/>
    </Route>
  </>
);

const rootElement = document.getElementById("root");
render(<App/>, rootElement);

