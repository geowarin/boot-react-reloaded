import * as React from "react";
import wretch from "wretch";

const api = wretch()
  .url("http://localhost:8080/api")
  .options({credentials: "include"});

api
  .url("/auth").post({userName: "admin", password: "admin"})
  .text().then(t => console.log(t));

api
  .url("/toto").get()
  .text().then(t => console.log(t));

class Test extends React.Component {
  componentDidMount(): void {

  }

  render() {
    return <div>Kikoo</div>;
  }
}

export default Test;
