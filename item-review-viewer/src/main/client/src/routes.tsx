import * as React from "react";
import { Router, Route, Switch } from "react-router";
import { BrowserRouter } from "react-router-dom";
import {
  Layout,
  SbNavlinkProps,
  ItemsSearchModel,
  ItemCardViewer,
  ErrorPageContainer,
  ItemViewerFrame
} from "@osu-cass/sb-components";

const siteLinks: SbNavlinkProps[] = [];


const url = "/ivs/items?ids=187-3000";

// Delete, testing
const body = (
  <div className="container test-container">
    <h2 className="page-title"> Page Title</h2>
    <div className="item-viewer">
      <ItemViewerFrame url={url} />
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
