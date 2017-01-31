package tds.iris.content;

import tds.iris.abstractions.repository.IContentBuilder;
import tds.iris.data.MetaData;

public interface IrisIContentBuilder extends IContentBuilder {
	public MetaData getMetadata(String id);

}
