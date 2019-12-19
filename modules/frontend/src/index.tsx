import * as React from "react";
import {render} from "react-dom";
import {Route} from "wouter";
import Toto from "./pages/toto";

const App = () => (
  <>
    <Route path="/">
      <Toto/>
    </Route>
  </>
);

const rootElement = document.getElementById("root");
render(<App/>, rootElement);

