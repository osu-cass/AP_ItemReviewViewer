import {
  SectionModel,
  RevisionModel,
  ItemRevisionModel,
  AccessibilityRevisionModel,
  AccResourceGroupModel,
  AboutItemRevisionModel,
  RubricModel
} from "@osu-cass/sb-components";

export async function mockPromise<T>(resolveVal: T) {
  return new Promise<T>(resolve => {
    resolve(resolveVal);
  });
}

export const mockBankAccessibilityClient = (acc: AccessibilityRevisionModel) =>
  mockPromise<AccResourceGroupModel[]>(allAccessibilityResourceGroups);

export const mockBankRevisionsClient = (item: ItemRevisionModel) =>
  mockPromise<RevisionModel[]>(revisions);

export const mockBankSectionsClient = () =>
  mockPromise<SectionModel[]>(sectionMocks);

const testDate = new Date("11/11/2015").toString();

export const revisions: RevisionModel[] = [
  {
    author: "Troy Barnes",
    date: testDate,
    commitMessage: "Added functionality to the website",
    commitHash: "ab65jg",
    selected: false
  },
  {
    author: "Pierce Hawthorne",
    date: testDate,
    commitMessage:
      "I want to see what happens when there is a much longer commit message than all of the rest",
    commitHash: "h4lso6",
    selected: false
  },
  {
    author: "Annie Edison",
    date: testDate,
    commitMessage: "Changed one of the pages",
    commitHash: "k5ls58",
    selected: false
  }
];

const sectionMocks: SectionModel[] = [
  {
    key: "SIW",
    value: "SIW"
  },
  {
    key: "MATH",
    value: "MATH"
  },
  {
    key: "IAT",
    value: "IAT"
  }
];

export const itemsMocks: ItemRevisionModel[] = [
  { bankKey: 187, itemKey: 3000, section: "siw" },
  { bankKey: 187, itemKey: 3200, section: "math" },
  { bankKey: 187, itemKey: 2700, section: "siw" },
  {}
];

export const allAccessibilityResourceGroups: AccResourceGroupModel[] = [
  {
    label: "Universal Tools",
    order: 0,
    accessibilityResources: [
      {
        resourceCode: "DigitalNotepad",
        currentSelectionCode: "TDS_SCNotepad",
        order: 1,
        defaultSelection: "TDS_SCNotepad",
        selections: [
          {
            selectionCode: "TDS_SCNotepad",
            label: "Notepad on",
            order: 1,
            disabled: false,
            hidden: false
          },
          {
            selectionCode: "TDS_SC0",
            label: "Notepad off",
            order: 2,
            disabled: false,
            hidden: false
          }
        ],
        label: "Digital Notepad",
        description:
          "This tool is used for making notes about an item. The digital notepad is item specific and is available through the end of the test segment. Notes are not saved when the student moves on to the next segment or after a break of more than 20 minutes. (EMBEDDED)",
        disabled: false
      }
    ]
  }
];

