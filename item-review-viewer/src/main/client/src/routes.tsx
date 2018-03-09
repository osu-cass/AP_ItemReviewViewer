import * as React from "react";
import { Router, Route, Switch } from "react-router";
import { BrowserRouter } from "react-router-dom";
import {
  Layout,
  SbNavlinkProps,
  ItemsSearchModel,
  ItemCardViewer,
  ErrorPageContainer,
  ItemBankContainer,
  ItemRevisionModel,
  ItemBankContainerProps,
  getResource
} from "@osu-cass/sb-components";
import { ItemBankPage } from "./ItemBankPage";

import {
  itemsMocks
} from "./Mocks";

const siteLinks: SbNavlinkProps[] = [];

// class IBWrapper extends React.Component<>

export const routes = (
  <Layout siteName="Item Bank Viewer" links={siteLinks}>
    <Switch>
      <Route exact path="/" render={props => <ItemBankPage />} />
      <Route path="*" render={props => <ErrorPageContainer />} />
    </Switch>
  </Layout>
);
