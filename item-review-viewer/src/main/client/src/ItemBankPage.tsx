import * as React from "react";
import { ItemBankContainerProps, ItemRevisionModel, ItemBankContainer } from "@osu-cass/sb-components";
import { RouteComponentProps } from "react-router";
import {
    accessibilityClient,
    aboutItemRevisionClient,
    revisionsClient,
    sectionsClient, namespacesClient
} from "./Clients/clients";
import {
    itemsMocks
} from "./Mocks";


interface ItemBankPageProps extends RouteComponentProps<ItemRevisionModel> { }
interface ItemBankPageState {
    itemUrl: string;
    id: string;
    version: string;
}

interface ScoreDetails extends Object {
    error: boolean;
    score: number;
}


export class ItemBankPage extends React.Component<ItemBankPageProps, ItemBankPageState> {
    constructor(props: ItemBankPageProps) {
        super(props);
        this.state = {
            itemUrl: "",
            id: "",
            version: "",
        };
        document.addEventListener('itemViewer:Response', (data: Event) => {
            console.log("Sending the item data");
            this.score();
        });
    }

    setItemUrl = (item: ItemRevisionModel) => {
        const { itemKey, bankKey, isaap, revision, section } = item;
        const itemUrl = `${window.location}ivs/items?ids=${bankKey}-${itemKey}`;
        if (revision) {
            itemUrl.concat(itemUrl, `&revision=${revision}`);
        }

        if (section) {
            itemUrl.concat(itemUrl, `&section=${section}`);
        }

        if (isaap) {
            itemUrl.concat(itemUrl, `&isaap=${isaap}`);
        }

        this.setState({ itemUrl , id: `${bankKey}-${itemKey}`, version: `${revision}` });
    }

    score = () => {
        const scoreEvent = new CustomEvent('itemViewer:Score', {
            bubbles: true,
            cancelable: false,
            detail: {
                id: this.state.id,
                version: this.state.version
            }
        });
        window.parent.document.dispatchEvent(scoreEvent);
    }

    render() {
        return (
            <div>
                <ItemBankContainer
                    accessibilityClient={accessibilityClient}
                    aboutItemRevisionClient={aboutItemRevisionClient}
                    revisionsClient={revisionsClient}
                    sectionsClient={sectionsClient}
                    namespacesClient={namespacesClient}
                    itemViewUrl={this.state.itemUrl}
                    setUrl={this.setItemUrl}
                    items={itemsMocks}
                />
            </div>
        );
    }
}
