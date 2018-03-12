import * as React from "react";
import { ItemBankContainerProps, ItemRevisionModel, ItemBankContainer } from "@osu-cass/sb-components";
import { RouteComponentProps } from "react-router";
import {
    accessibilityClient,
    aboutItemRevisionClient,
    revisionsClient,
    sectionsClient
  } from "./Clients/clients";
import {
    itemsMocks
  } from "./Mocks";


interface ItemBankPageProps extends RouteComponentProps<ItemRevisionModel> { }
interface ItemBankPageState {
    itemUrl: string;
}
export class ItemBankPage extends React.Component<ItemBankPageProps, ItemBankPageState> {
    constructor ( props: ItemBankPageProps) {
        super( props );
        this.state = {
            itemUrl: ""
        };
    }

    getItemUrl = ( item: ItemRevisionModel ) => {
        const { itemKey, bankKey, isaap } = item;
        const itemUrl = `${ window.location }ivs/items?ids=${ bankKey }-${ itemKey }`;
        if ( isaap ) {
            itemUrl.concat( itemUrl, `&isaap=${ isaap }` );
        }
        this.setState( { itemUrl } );

        return itemUrl;
    }

    render () {
        return (
            <ItemBankContainer
            accessibilityClient={accessibilityClient}
            aboutItemRevisionClient={aboutItemRevisionClient}
            revisionsClient={revisionsClient}
            sectionsClient={sectionsClient}
            itemViewUrl={this.state.itemUrl}
            getUrl={this.getItemUrl}
            items={itemsMocks}
          />);
    }
}