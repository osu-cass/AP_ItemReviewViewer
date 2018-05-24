package org.smarterbalanced.itemreviewviewer.web.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemCommit;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemDocument;
import org.smarterbalanced.itemreviewviewer.web.services.models.Metadata;
import tds.itemrenderer.data.IITSDocument;

public interface IGitLabService {

    boolean downloadItem(String itemNumber) throws GitLabException;

    boolean isItemExistsLocally(String itemNumber);

    String unzip(String itemNumber) throws IOException;

    ItemDocument getItemScoring(String itemNumber);

    List<ItemCommit> getItemCommits(String itemNumber) throws GitLabException;

    List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException;

    Metadata getMetadata(String itemNumber) throws GitLabException;

    ItemMetadataModel getItemMetadata(String itemId, String section) throws GitLabException, FileNotFoundException;

    void downloadAssociatedItems(IITSDocument doc);
}
