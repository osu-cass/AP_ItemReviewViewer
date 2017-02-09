package tds.iris.web.handlers;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import AIR.Common.Utilities.SpringApplicationContext;
import tds.iris.content.ItemViewerContentBuilder;
import tds.iris.data.MetaData;

/**
 * @author ssuryadevara
 *
 */
@ManagedBean(name = "metadataBacking")
@Scope(value = "request")
public class MetadataBacking {
	private ItemViewerContentBuilder contentBuilder;
	private static final Logger _logger = LoggerFactory.getLogger(MetadataBacking.class);
	private MetaData metadata = null;

	public MetadataBacking() {
		init();

	}

	private void init() {
		contentBuilder = SpringApplicationContext.getBean("iContentBuilder", ItemViewerContentBuilder.class);

	}

	public MetaData getMetadata() {
		if (metadata == null) {
			try {
				String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("type");
				String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("bankId");
				String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("id");
				StringBuffer id = new StringBuffer();
				if (StringUtils.hasLength(type) && StringUtils.hasLength(bankId) && StringUtils.hasLength(itemNumber)) {

					id.append(type + "-" + bankId + "-" + itemNumber + "-");
				}
				String version = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("version");
				if (StringUtils.hasLength(version))
					id.append(version);

				metadata = contentBuilder.getMetadata(id.toString());

			} catch (Exception e) {
				_logger.error("exception occcurred while getting metadata", e);
				return null;
			}
		}
		return metadata;
	}

}
