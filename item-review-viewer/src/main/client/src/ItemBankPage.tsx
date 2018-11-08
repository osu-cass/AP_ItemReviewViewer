import * as React from "react";
import {ItemBankContainerProps, ItemRevisionModel, ItemBankContainer, NamespaceModel, Subscription} from "@osu-cass/sb-components";
import {RouteComponentProps} from "react-router";

import {
    accessibilityClient,
    aboutItemRevisionClient,
    revisionsClient,
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

interface KeyValuePair {
    key: string;
    value: string;
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
        const { itemKey, bankKey, isaap, revision, namespace } = item;
        let itemUrl = `${window.location.origin}/ivs/items?ids=${bankKey}-${itemKey}`;

        if (revision) {
            itemUrl = `${itemUrl}-${revision}`;
        }

        if (isaap) {
            itemUrl = `${itemUrl}&isaap=${isaap}`;
        }

        itemUrl = `${itemUrl}&namespace=${namespace}`;

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

    findNamespaceObject(bankKey?: number, namespace?: string) : NamespaceModel | undefined {
        const { namespaces } = this.state;

        return namespaces.find(s => s.name.toLowerCase() === (namespace  ? namespace.toLowerCase() : "") || s.bankKey === bankKey);
    }

    parseUrl = (search: string): ItemRevisionModel | undefined  => {
        const searchSplit = search.split("&");
        const dic: KeyValuePair[] = [];
        //tslint:disable
        searchSplit.forEach( function (param) {
            const keyValuePair = param.split("=");
            if(keyValuePair.length === 2) {
                dic.push({key: keyValuePair[0], value: keyValuePair[1]});
            }
        });

        //tslint:disable
        const itemKey = dic.find(function(kv) { return kv.key === 'itemKey';});
        const bankKey = dic.find(function(kv) { return kv.key === 'bankKey';});
        const namespace = dic.find(function(kv) { return kv.key === 'namespace';});
        const revision = dic.find(function (kv) { return  kv.key === 'revision';});

        let item : ItemRevisionModel | undefined = {
            itemKey: itemKey ? parseInt(itemKey.value, 10) : undefined,
            bankKey: bankKey ? parseInt(bankKey.value, 10) : undefined,
            namespace: namespace ? namespace.value : undefined,
            revision: revision ? revision.value : undefined,
        };

        if(!item.bankKey && item.namespace){
            const namespaceObject: NamespaceModel | undefined = this.findNamespaceObject(undefined, item.namespace);
            item.bankKey = namespaceObject ? namespaceObject.bankKey : undefined;
        }

        if (!item.itemKey && item.bankKey  && item.namespace) {
            item = undefined;
        }

        return item;
    }

    getItemFromUrl () {
        let items: ItemRevisionModel[] = [];
        if(this.props.location.search){
            let parsedItem: ItemRevisionModel | undefined = this.parseUrl(this.props.location.search.split('?')[1]);
            let itemUrl = this.state.itemUrl;
            if (parsedItem && parsedItem.itemKey && parsedItem.namespace) {
                if (!parsedItem.bankKey && parsedItem.namespace) {
                    const namespaceObject = this.findNamespaceObject(undefined, parsedItem.namespace);
                    parsedItem = {...parsedItem, bankKey: namespaceObject ? namespaceObject.bankKey : undefined };
                } else if(parsedItem.bankKey && !parsedItem.namespace){
                    const namespaceObject = this.findNamespaceObject(parsedItem.bankKey);
                    console.log(namespaceObject);
                    parsedItem = {...parsedItem, namespace: namespaceObject ? namespaceObject.name : undefined};
                    console.log(parsedItem);
                }

                items = [parsedItem];
            }
        }
        items.push({});

        return items;
    }

    render() {
        const items = this.getItemFromUrl();
        let itemUrl = this.state.itemUrl;
        if(items.length > 1) {
            itemUrl = `ivs/items?ids=${items[0].bankKey}-${items[0].itemKey}&namespace=${items[0].namespace}`;
        }

        return (
            <div>
                <ItemBankContainer
                    accessibilityClient={accessibilityClient}
                    aboutItemRevisionClient={aboutItemRevisionClient}
                    revisionsClient={revisionsClient}
                    namespaces={this.state.namespaces}
                    itemExistsClient={itemExistsClient}
                    itemViewUrl={itemUrl}
                    setUrl={this.setItemUrl}
                    resetUrl={this.resetUrl}
                    items={items}
                />
            </div>
        );
    }
}
