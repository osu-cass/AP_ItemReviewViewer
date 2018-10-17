import * as React from "react";
import { ItemBankContainerProps, ItemRevisionModel, ItemBankContainer } from "@osu-cass/sb-components";
import { RouteComponentProps } from "react-router";
import {
    accessibilityClient,
    aboutItemRevisionClient,
    revisionsClient,
    sectionsClient,
    namespacesClient,
    itemExistsClient
} from "./Clients/clients";
import {
    itemsMocks
} from "./Mocks";

interface ItemBankPageState {
    itemUrl?: string;
    id: string;
    version: string;
}

interface ScoreDetails extends Object {
    error: boolean;
    score: number;
}


export class ItemBankPage extends React.Component<RouteComponentProps<{}>, ItemBankPageState> {
    constructor(props: RouteComponentProps<{}>) {
        super(props);
        this.state = {
            itemUrl: "",
            id: "",
            version: "",
        };
        document.addEventListener('itemViewer:Response', (data: Event) => {
            this.score();
        });
    }

    setItemUrl = (item: ItemRevisionModel) => {
        console.log(item);
        const { itemKey, bankKey, isaap, revision, section } = item;
        let itemUrl = `${window.location}ivs/items?ids=${bankKey}-${itemKey}`;
        if (revision) {
            itemUrl = `${itemUrl}-${revision}`;
        }

        if (section) {
            itemUrl = `${itemUrl}&section=${section}`;
        }

        if (isaap) {
            itemUrl = `${itemUrl}&isaap=${isaap}`;
        }

        console.log(itemUrl);
        this.setState({ itemUrl, id: `${bankKey}-${itemKey}`, version: `${revision}` });
    }

    resetUrl = () => {
        this.setState({ itemUrl: undefined });
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
                    itemExistsClient={itemExistsClient}
                    itemViewUrl={this.state.itemUrl}
                    setUrl={this.setItemUrl}
                    resetUrl={this.resetUrl}
                    items={[{}]}
                />
            </div>
        );
    }
}
