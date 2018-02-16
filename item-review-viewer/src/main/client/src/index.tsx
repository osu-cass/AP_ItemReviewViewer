import * as React from "react";
import * as ReactDOM from "react-dom";
import "./Styles/bundle.less";
import { routes } from "./routes";
import { BrowserRouter } from "react-router-dom";

function renderApp() {
  ReactDOM.render(
    <BrowserRouter children={routes} basename={"/"} />,
    document.getElementById("react-app")
  );
}

renderApp();