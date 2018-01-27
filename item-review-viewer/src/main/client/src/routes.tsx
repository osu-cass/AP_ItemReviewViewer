import * as React from "react";
import { Router, Route, Switch } from "react-router";
import { BrowserRouter } from "react-router-dom";
import {
  Layout,
  SbNavlinkProps,
  ItemsSearchModel,
  ItemCardViewer,
  ErrorPageContainer
} from "@osu-cass/sb-components";

const siteLinks: SbNavlinkProps[] = [];

// Delete me, testing
const style: React.CSSProperties = {
  width: "100%",
  height: "70vh"
};

// Delete, testing
const body = (
  <div className="container test-container">
    <h2 className="page-title">Page Title</h2>
    <div className="section section-light" style={style}>
      <p>Site content</p>
    </div>
  </div>
);

export const routes = (
  <Layout siteName="Item Bank Viewer" links={siteLinks}>
    <Switch>
      <Route exact path="/" render={props => body} />
      <Route path="*" render={props => <ErrorPageContainer />} />
    </Switch>
  </Layout>
);
