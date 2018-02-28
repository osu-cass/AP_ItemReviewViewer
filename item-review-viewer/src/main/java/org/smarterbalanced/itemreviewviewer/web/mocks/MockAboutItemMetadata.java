package org.smarterbalanced.itemreviewviewer.web.mocks;

import org.smarterbalanced.itemreviewviewer.web.models.ItemMetadata;

public class MockAboutItemMetadata {

    public static ItemMetadata getMetadata(String itemKey, String bankKey, String revision, String section){
        ItemMetadata meta = new ItemMetadata(itemKey, bankKey, revision, section);
        return meta;
    }
}
