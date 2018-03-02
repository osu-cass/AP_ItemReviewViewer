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
    return getRequest<AccResourceGroupModel[]>( "/api/GetAccessibility/", acc );
};

export const aboutItemRevisionClient = ( item: ItemRevisionModel ):
    Promise<AboutItemRevisionModel> => {
    return getRequest<AboutItemRevisionModel>( "/api/AboutItem", item );
};

export const revisionsClient = ( item: ItemRevisionModel ):
    Promise<RevisionModel[]> => {
    return getRequest<RevisionModel[]>( "/api/ItemRevisions", item );
};

export const sectionsClient = (): Promise<SectionModel[]> => {
    return getRequest<SectionModel[]>("/api/BankSections");
};

