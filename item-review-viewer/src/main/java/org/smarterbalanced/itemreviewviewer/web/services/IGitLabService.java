package org.smarterbalanced.itemreviewviewer.web.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.smarterbalanced.itemreviewviewer.web.services.models.ItemRelease;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.ItemMetadataModel;
import org.smarterbalanced.itemreviewviewer.web.services.models.Namespace;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemCommit;
import org.smarterbalanced.itemreviewviewer.web.services.models.ItemDocument;
import org.smarterbalanced.itemreviewviewer.web.services.models.Metadata;
import tds.itemrenderer.data.IITSDocument;

public interface IGitLabService {

    boolean downloadItem(String namespace, String itemNumber) throws GitLabException;

    boolean isItemExistsLocally(String itemNumber);

    void unzip(String namespace, String itemNumber) throws IOException;

    List<Namespace> getNamespaces();

    ItemDocument getItemScoring(String namespace, String itemNumber);

    List<ItemCommit> getItemCommits(String namespace, String itemNumber) throws GitLabException;

    List<ItemCommit> getItemCommits(String namespace, String type, String bankId, String itemNumber) throws GitLabException;

    Metadata getMetadata(String namespace, String itemNumber) throws GitLabException;

    ItemMetadataModel getItemMetadata(String namespace, String itemId, String section) throws GitLabException, FileNotFoundException;

    ItemRelease getItemData (String namespace, String itemId) throws GitLabException;

    String getClaim(String itemNumber) throws GitLabException;

    boolean isItemAccomExists (String itemNumber, String ext);
}
