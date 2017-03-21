package org.smarterbalanced.irv.web.content;

import org.smarterbalanced.irv.core.model.MetaData;

import tds.iris.abstractions.repository.IContentBuilder;

public interface IrisIContentBuilder extends IContentBuilder {
	public MetaData getMetadata(String id);

}
