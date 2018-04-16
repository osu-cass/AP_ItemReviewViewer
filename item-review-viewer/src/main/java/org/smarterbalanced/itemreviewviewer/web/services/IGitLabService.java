package org.smarterbalanced.itemreviewviewer.web.services;

import java.io.IOException;
import java.util.List;

import org.smarterbalanced.itemreviewviewer.web.models.*;
import tds.itemrenderer.data.IITSDocument;

public interface IGitLabService {

    boolean downloadItem(String itemNumber) throws GitLabException;

    boolean isItemExistsLocally(String itemNumber);

    String unzip(String itemNumber) throws IOException;

    ItemDocument getItemScoring(String itemName);

    List<ItemCommit> getItemCommits(String itemName) throws GitLabException;

    List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException;

    Metadata getMetadata(String itemNumber) throws GitLabException;

    ItemMetadataModel getItemMetadata(String itemId, String section) throws GitLabException;

    void downloadAssociatedItems(IITSDocument doc);
}
