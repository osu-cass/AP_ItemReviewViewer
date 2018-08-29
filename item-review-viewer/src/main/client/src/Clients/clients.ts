import {
    getRequest,
    AccessibilityRevisionModel,
    AccResourceGroupModel,
    ItemRevisionModel,
    AboutItemRevisionModel,
    RevisionModel,
    SectionModel,
    NamespaceModel
} from "@osu-cass/sb-components";

export const accessibilityClient = ( acc: AccessibilityRevisionModel ):
    Promise<AccResourceGroupModel[]> => {
    return getRequest<AccResourceGroupModel[]>("/api/GetAccessibility", {...acc});
};

export const aboutItemRevisionClient = ( item: ItemRevisionModel ):
    Promise<AboutItemRevisionModel> => {
        return getRequest<AboutItemRevisionModel>( "/renderitem/",{...item} );
};

export const revisionsClient = ( item: ItemRevisionModel ):
    Promise<RevisionModel[]> => {
    return getRequest<RevisionModel[]>( "/renderitem/revisions", {...item} );
};

export const sectionsClient = (): Promise<SectionModel[]> => {
    return getRequest<SectionModel[]>("/renderitem/banksections");
};

export const namespacesClient = (): Promise<NamespaceModel[]> => {
    return getRequest<NamespaceModel[]>("/renderitem/namespaces");
};