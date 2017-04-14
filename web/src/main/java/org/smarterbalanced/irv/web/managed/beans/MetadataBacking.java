package org.smarterbalanced.irv.web.managed.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarterbalanced.irv.model.Metadata;
import org.smarterbalanced.irv.services.GitLabService;
import org.smarterbalanced.irv.services.IGitLabService;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

/**
 * @author ssuryadevara
 *
 */
@ManagedBean(name = "metadataBacking")
@Scope(value = "request")
public class MetadataBacking {

	private static final Logger _logger = LoggerFactory.getLogger(MetadataBacking.class);
	private Metadata metadata;
	private boolean metadataChecked = false;

	public MetadataBacking() {

	}

	public Metadata getMetadata() {
		if (metadata == null && !metadataChecked) {
			try {
				String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type");
				String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bankId");
				String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

				StringBuffer id = new StringBuffer();
				if (StringUtils.hasLength(type) && StringUtils.hasLength(bankId) && StringUtils.hasLength(itemNumber)) {
					id.append(type + "-" + bankId + "-" + itemNumber);
				}

				String version = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("version");
				if (StringUtils.hasLength(version)) {
					id.append("-");
					id.append(version);
				}

				if (StringUtils.hasLength(id.toString())) {
					IGitLabService gitLabService = new GitLabService();
					metadata = gitLabService.getMetadata(id.toString());
					metadataChecked = true;
				}

			} catch (Exception e) {
				_logger.error("exception occcurred while getting metadata", e);
				return null;
			}
		}
		return metadata;
	}

	public boolean isDisableCalculator() {
		try {
			return getMetadata() == null
					||getMetadata().getSmarterAppMetadata() == null
					|| getMetadata().getSmarterAppMetadata().getAllowCalculator() == null
					|| "N".equalsIgnoreCase(getMetadata().getSmarterAppMetadata().getAllowCalculator());

		} catch (Exception e) {
			_logger.error("unknown exception occcurred while getting calculator metadata", e);
			return false;
		}
	}

	public boolean isDisableSpanish() {
		try {
			return getMetadata() == null
					|| getMetadata().getSmarterAppMetadata() == null
					|| getMetadata().getSmarterAppMetadata().getLanguage() == null
					|| getMetadata().getSmarterAppMetadata().getLanguage().size() < 2;

		} catch (Exception e) {
			_logger.error("unknown exception occcurred while getting spanish metadata", e);

			return true;
		}

	}

}
