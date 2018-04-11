package org.smarterbalanced.irv.web.managed.beans;

import java.lang.Object;
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
	private  final String ITEM_TYPE="type";
	private  final String ITEM_BANK="bankId";
	private  final String ITEM_ID="id";
	private  final String ITEM_VERSION="version";
	private  final String ALLOW_CALC="N";
	private  final String ALLOW_DICT_AND_CLOSDCAP="ELA";

	public MetadataBacking() {

	}

	public Metadata getMetadata() {
		if (metadata == null && !metadataChecked) {
			try {
				String type = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(ITEM_TYPE);
				String bankId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(ITEM_BANK);
				String itemNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(ITEM_ID);

				StringBuffer id = new StringBuffer();
				if (StringUtils.hasLength(type) && StringUtils.hasLength(bankId) && StringUtils.hasLength(itemNumber)) {
					id.append(type + "-" + bankId + "-" + itemNumber);
				}

				String version = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(ITEM_VERSION);
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
					|| ALLOW_CALC.equalsIgnoreCase(getMetadata().getSmarterAppMetadata().getAllowCalculator());

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
	public boolean isDisableDictionaryandClosedCaption() {
		try {
			return getMetadata() == null
					|| getMetadata().getSmarterAppMetadata() == null
					|| getMetadata().getSmarterAppMetadata().getSubject() == null
					|| !ALLOW_DICT_AND_CLOSDCAP.equalsIgnoreCase(getMetadata().getSmarterAppMetadata().getSubject());

		} catch (Exception e) {
			_logger.error("unknown exception occcurred while getting item subject", e);

			return true;
		}

	}

}
