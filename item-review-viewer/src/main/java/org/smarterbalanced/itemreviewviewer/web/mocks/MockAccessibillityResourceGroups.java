package org.smarterbalanced.itemreviewviewer.web.mocks;

import org.smarterbalanced.itemreviewviewer.web.models.Accessibility.AccessibilityResourceGroupModel;
import org.smarterbalanced.itemreviewviewer.web.models.Accessibility.AccessibilityResourceModel;
import org.smarterbalanced.itemreviewviewer.web.models.Accessibility.DropDownSelectionModel;

import java.util.ArrayList;
import java.util.List;

public class MockAccessibillityResourceGroups {
    public List<AccessibilityResourceGroupModel> mockModel;

    public MockAccessibillityResourceGroups(){
        mockModel = new ArrayList<AccessibilityResourceGroupModel>();
        List<AccessibilityResourceModel> armList = new ArrayList<AccessibilityResourceModel>();
        List<DropDownSelectionModel> dmList = new ArrayList<DropDownSelectionModel>();

        DropDownSelectionModel dm1 = new DropDownSelectionModel(false, false, "Notepad on", "TDS_SCNotepad", 1);
        DropDownSelectionModel dm2 = new DropDownSelectionModel(false, false, "Notepad off", "TDS_SC0", 2);

        dmList.add(dm1);
        dmList.add(dm2);

        AccessibilityResourceModel arm1 = new AccessibilityResourceModel(false, "DigitalNotepad", "TDS_SCNotepad", "\"This tool is used for making notes about an item. The digital notepad is item specific and is available through the end of the test segment. Notes are not saved when the student moves on to the next segment or after a break of more than 20 minutes. (EMBEDDED)","Digital Notepad", "TDS_SCNotepad", 1, dmList,);

        armList.add(arm1);

        AccessibilityResourceGroupModel argm = new AccessibilityResourceGroupModel("Universat Tools", 0, armList);

        mockModel.add(argm);
    }

    public List<AccessibilityResourceGroupModel> getMockModel() {
        return mockModel;
    }
}
