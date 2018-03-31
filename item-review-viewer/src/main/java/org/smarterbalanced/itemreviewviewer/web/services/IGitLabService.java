package org.smarterbalanced.itemreviewviewer.web.services;

import java.io.IOException;
import java.util.List;

import org.smarterbalanced.itemreviewviewer.web.models.ItemCommit;
import org.smarterbalanced.itemreviewviewer.web.models.Metadata;
import tds.itemrenderer.data.IITSDocument;

public interface IGitLabService {

    boolean downloadItem(String itemNumber) throws GitLabException;

    Metadata getMetadata(String itemNumber) throws GitLabException;

    void downloadAssociatedItems(IITSDocument doc);

    boolean isItemExistsLocally(String itemNumber);

    String unzip(String itemNumber) throws IOException;

    List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException;

}
