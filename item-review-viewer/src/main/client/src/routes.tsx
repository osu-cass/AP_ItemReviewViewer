import * as React from "react";
import { Router, Route, Switch } from "react-router";
import { BrowserRouter } from "react-router-dom";
import {
  Layout,
  SbNavlinkProps,
  ItemsSearchModel,
  ItemCardViewer,
  ErrorPageContainer,
  ItemViewerFrame,
  ItemBankContainer
} from "@osu-cass/sb-components";

import {
  mockBankAboutItemClient,
  mockBankAccessibilityClient,
  mockBankRevisionsClient,
  mockBankSectionsClient,
  itemsMocks
} from "./mocks";

const siteLinks: SbNavlinkProps[] = [];

const url = "/ivs/items?ids=187-3000";

export const routes = (
  <Layout siteName="Item Bank Viewer" links={siteLinks}>
    <Switch>
      <Route
        exact
        path="/"
        render={props => (
          <ItemBankContainer
            accessibilityClient={mockBankAccessibilityClient}
            aboutItemRevisionClient={mockBankAboutItemClient}
            revisionsClient={mockBankRevisionsClient}
            itemViewUrl=""
            sectionsClient={mockBankSectionsClient}
            items={itemsMocks}
          />
        )}
      />
      <Route path="*" render={props => <ErrorPageContainer />} />
    </Switch>
  </Layout>
);
