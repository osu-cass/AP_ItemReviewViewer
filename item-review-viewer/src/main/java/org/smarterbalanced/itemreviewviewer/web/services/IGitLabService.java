package org.smarterbalanced.itemreviewviewer.web.services;

import java.io.IOException;

import org.smarterbalanced.itemreviewviewer.web.models.Metadata;

public interface IGitLabService {

    boolean downloadItem(String itemNumber) throws GitLabException;

    Metadata getMetadata(String itemNumber) throws GitLabException;

    boolean isItemExistsLocally(String itemNumber);

    String unzip(String itemNumber) throws IOException;

    //List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException;

}
