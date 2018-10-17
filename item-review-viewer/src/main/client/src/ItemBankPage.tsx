import * as React from "react";
import {ItemBankContainerProps, ItemRevisionModel, ItemBankContainer, NamespaceModel, Subscription} from "@osu-cass/sb-components";
import {RouteComponentProps} from "react-router";

import * as QueryParser from  'query-string';

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
    namespaces: NamespaceModel[];
}

interface ScoreDetails extends Object {
    error: boolean;
    score: number;
}


export class ItemBankPage extends React.Component<RouteComponentProps<{}>, ItemBankPageState> {
    private subscription = new Subscription();

    constructor(props: RouteComponentProps<{}>) {
        super(props);
        this.state = {
            namespaces: [],
            itemUrl: '',
            id: '',
            version:''
        };
        document.addEventListener('itemViewer:Response', (data: Event) => {
            this.score();
        });
        this.fetchNamespaces();
    }

    componentDidMount() {
        this.fetchNamespaces().then(namespaces => {
            this.setState({namespaces});
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

    async fetchNamespaces() {
        const prom = namespacesClient();
        const promiseWrapper = this.subscription.add('namespacesClient', prom);

        return promiseWrapper.promise;
    }

    getItemFromUr () {
        const queryString = this.props.location.toString().split('?')[1];
        if(queryString) {

        } else {

        }

    }

    render() {
        return (
            <div>
                <ItemBankContainer
                    accessibilityClient={accessibilityClient}
                    aboutItemRevisionClient={aboutItemRevisionClient}
                    revisionsClient={revisionsClient}
                    sectionsClient={sectionsClient}
                    namespaces={this.state.namespaces}
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
