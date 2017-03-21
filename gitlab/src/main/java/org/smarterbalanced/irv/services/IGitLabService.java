package org.smarterbalanced.irv.services;

import java.io.IOException;
import java.util.List;

import org.smarterbalanced.irv.model.Metadata;
import org.smarterbalanced.irv.model.ItemCommit;

public interface IGitLabService {

	boolean downloadItem(String itemNumber) throws GitLabException;

	Metadata getMetadata(String itemNumber) throws GitLabException;

	boolean isItemExistsLocally(String itemNumber);

	String unzip(String itemNumber) throws IOException;

	List<ItemCommit> getItemCommits(String type, String bankId, String itemNumber) throws GitLabException;

}