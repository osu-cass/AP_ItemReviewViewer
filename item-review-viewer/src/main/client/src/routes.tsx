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
  getResource
} from "@osu-cass/sb-components";
import {
  accessibilityClient,
  aboutItemRevisionClient,
  revisionsClient,
  sectionsClient
} from "./Clients/clients";

const siteLinks: SbNavlinkProps[] = [];

const url = "/ivs/items?ids=187-3000";

export const routes = (
  <Layout siteName="Item Bank Viewer" links={siteLinks}>
    <Switch>
      <Route exact path="/" render={props =>
        <ItemBankContainer
          accessibilityClient={accessibilityClient}
          aboutItemRevisionClient={aboutItemRevisionClient}
          revisionsClient={revisionsClient}
          sectionsClient={sectionsClient}
          itemViewUrl={url}
        />} />
      <Route path="*" render={props => <ErrorPageContainer />} />
    </Switch>
  </Layout>
);
