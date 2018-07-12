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
    id: string;
    version: string;
}

export class ItemBankPage extends React.Component<ItemBankPageProps, ItemBankPageState> {
    constructor(props: ItemBankPageProps) {
        super(props);
        this.state = {
            itemUrl: "",
            id: "",
            version: "",
        };
        window.parent.addEventListener('itemViewer:Response', (data: Event) => { 
            this.handleResponse(data as CustomEvent);
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
        this.setState({ itemUrl: itemUrl, id: `${bankKey}-${itemKey}`, version: `${revision}` });
    }

    handleResponse = (data: CustomEvent) => {
        if(data.detail.error)
            alert("Error")
        else
            alert(data.detail.score)
    }

	score = () => {
        var scoreEvent = new CustomEvent('itemViewer:Score', {
            bubbles: true, 
            cancelable: false,
            detail: {
                id: this.state.id,
                version: this.state.version
            }
        })
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
                itemViewUrl={this.state.itemUrl}
                setUrl={this.setItemUrl}
                items={itemsMocks}
            />
            <div>
                <div id="incorrectAlert" hidden></div>
                <div id="correctAlert" hidden></div>
                <div id="noPoints" hidden></div>
                <div id="versionDisplay" hidden></div>
                <div className="item-nav-right-group"><button className="btn btn-primary" onClick={this.score}>Score</button></div>
			</div>
            </div>
			);
    }
}
