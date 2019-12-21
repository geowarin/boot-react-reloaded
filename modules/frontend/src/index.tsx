import * as React from "react";
import {render} from "react-dom";
import {Link, Redirect, Route, Switch} from "wouter";
import MainPage from "./pages/MainPage";
import LoginForm from "./pages/LoginForm";
import {api} from "./api";
import {useAsync} from "react-async-hook";

const menuItems = [
  {label: 'Home', link: '/'},
  {label: 'Logout', link: '/logout'},
  {label: 'Private page', link: '/private'}
];

const TopMenu: React.FC = () => {
  const items = menuItems.map((item, key) => (
    <li key={key} className="pure-menu-item">
      <Link to={item.link} className="pure-menu-link">{item.label}</Link>
    </li>
  ));
  return (
    <div className="pure-menu pure-menu-horizontal">
      <ul className="pure-menu-list">
        {items}
      </ul>
    </div>
  );
};

const RouteLayout: React.FC<{ path: string }> = ({path, children}) => {
  return (
    <Route path={path}>
      <div className="app-container">
        <TopMenu/>
        <div className="app-content">
          {children}
        </div>
      </div>
    </Route>
  );
};

const Logout: React.FC = () => {
  const {loading, error} = useAsync(async () => {
    return api.url("/api/auth").delete();
  },[]);
  if (loading) {
    return <h2>Login out...</h2>
  }
  if (error) {
    return <h2>Error while login out</h2>
  }
  return (
    <Redirect to="/login"/>
  )
};

const App = () => (
  <Switch>
    <RouteLayout path="/">
      <MainPage/>
    </RouteLayout>
    <Route path="/toto">toto</Route>
    <Route path="/login"><LoginForm/></Route>
    <Route path="/logout"><Logout/></Route>
    <Route path="/:rest*">404</Route>
  </Switch>
);

const rootElement = document.getElementById("root");
render(<App/>, rootElement);

