package org.smarterbalanced.itemreviewviewer.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemIdUtils {
    private static final Logger _logger = LoggerFactory.getLogger(ItemIdUtils.class);

    public static class ItemId {
        String prefix;
        String bankKey;
        String id;
        String commitId;
        boolean hasFullId;

        String makeItemFileName() {
            return String.format("%s-%s-%s", prefix, bankKey, id);
        }
    }

    static ItemId parseItemId(String itemNumbers) {
        ItemId itemId = new ItemId();

        String[] parts = itemNumbers.split("-");
        if (parts.length > 4) {
            //TODO: regard it error
            return null;
        }

        if (parts.length == 1) {
            itemId.bankKey = parts[0];
            itemId.hasFullId = false;
            return itemId;
        }

        itemId.prefix = parts[0];
        itemId.bankKey = parts[1];
        itemId.id = parts[2];
        itemId.hasFullId = true;

        if (parts.length == 4) {
            itemId.commitId = parts[3];
        }

        return itemId;
    }
}
