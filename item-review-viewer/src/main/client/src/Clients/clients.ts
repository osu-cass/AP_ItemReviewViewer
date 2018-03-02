import {
    getRequest,
    AccessibilityRevisionModel,
    AccResourceGroupModel,
    ItemRevisionModel,
    AboutItemRevisionModel,
    RevisionModel,
    SectionModel
} from "@osu-cass/sb-components";

export const accessibilityClient = ( acc: AccessibilityRevisionModel ):
    Promise<AccResourceGroupModel[]> => {
    return getRequest( "item/revision/accessibility", acc );
};

export const aboutItemRevisionClient = ( item: ItemRevisionModel ):
    Promise<AboutItemRevisionModel> => {
    return getRequest( "item/revision/item", item );
};

export const revisionsClient = ( item: ItemRevisionModel ):
    Promise<RevisionModel[]> => {
    return getRequest( "item/revision/revisions", item );
};

export const sectionsClient = (): Promise<SectionModel[]> => {
    return getRequest("item/sections");
};

