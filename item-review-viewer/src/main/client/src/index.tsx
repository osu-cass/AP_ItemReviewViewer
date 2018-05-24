import * as React from "react";
import * as ReactDOM from "react-dom";
import "./Styles/bundle.less";
import { routes } from "./routes";
import { BrowserRouter } from "react-router-dom";
import * as ReactModal from "react-modal";
// tslint:disable-next-line:no-require-imports no-submodule-imports no-var-requires
require("es6-promise/auto");

function renderApp () {
  ReactDOM.render(
    <BrowserRouter children={routes} basename={"/"} />,
    document.getElementById( "react-app" )
  );
}

renderApp();
ReactModal.setAppElement( "#main" );