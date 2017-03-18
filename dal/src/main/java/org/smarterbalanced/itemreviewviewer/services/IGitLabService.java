package org.smarterbalanced.itemreviewviewer.services;

import java.io.IOException;
import java.util.List;

import org.smarterbalanced.itemreviewviewer.model.ItemCommit;
import org.smarterbalanced.itemreviewviewer.model.MetaData;

public interface IGitLabService {

	boolean downloadItem(String itemNumber) throws ContentException;

	MetaData getMetaData(String itemNumber);

	boolean isItemExistsLocally(String itemNumber);

	String unzip(String itemNumber) throws IOException;

	String getGitLabItemUrl(String itemName);

	List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber);

}