package org.smarterbalanced.itemreviewviewer.content;

import org.smarterbalanced.itemviewerservice.core.Models.MetaData;
import tds.iris.abstractions.repository.IContentBuilder;

public interface IrisIContentBuilder extends IContentBuilder {
	public MetaData getMetadata(String id);

}
