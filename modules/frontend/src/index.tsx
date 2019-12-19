// import * as React from "react";
// import {render} from "react-dom";
// import {Route} from "wouter";
// import Toto from "./pages/toto";
// import Test from "./pages/test";
//
// const App = () => (
//   <>
//     <Route path="/">
//       <Toto/>
//     </Route>
//     <Route path="/test">
//       <Test/>
//     </Route>
//   </>
// );
//
// const rootElement = document.getElementById("root");
// render(<App/>, rootElement);
//

import wretch from "wretch";

const api = wretch()
  .url("http://localhost:8080/api")
  .options({credentials: "include"});

const toto = async () => {

  const token = await api
    .url("/auth").post({userName: "admin", password: "admin"})
    .text();

  console.log("token", token);

  const result = await api
    .url("/toto").get()
    .text();

  console.log("result", result);
};

toto();
