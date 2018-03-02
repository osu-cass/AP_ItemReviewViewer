import {
  getRequest,
  AccessibilityRevisionModel,
  ItemRevisionModel,
  AboutItemRevisionModel,
  RevisionModel,
  SectionModel,
  AccResourceGroupModel
} from "@osu-cass/sb-components";


// This will point to SIW soon
export const accessibilityClient = (
  props: AccessibilityRevisionModel
) => getRequest<AccResourceGroupModel[]>("/api/GetAccessibility/", props);

export const aboutItemBankClient = (props: ItemRevisionModel) =>
  getRequest<AboutItemRevisionModel>("/api/AboutItem", props);

export const aboutItemRevisionClient = (props: ItemRevisionModel) =>
  getRequest<RevisionModel[]>("/api/ItemRevisions", props);

export const bankSectionClient = () =>
  getRequest<SectionModel[]>("/api/BankSections");
