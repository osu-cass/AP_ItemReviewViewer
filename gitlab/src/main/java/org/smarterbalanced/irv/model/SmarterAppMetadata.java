package org.smarterbalanced.irv.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "smarterAppMetadata")
@XmlType(propOrder = { "identifier", "itemAuthorIdentifier", "itemSpecFormat", "lastModifiedBy", "securityStatus",
		"smarterAppItemDescriptor", "status", "stimulusFormat", "subject", "version", "intendedGrade", "minimumGrade",
		"maximumGrade", "depthOfKnowledge", "interactionType", "maximumNumberOfPoints", "allowCalculator",
		"copyrightAndOtherRestrictions", "brailleType", "enemyItem", "standardPublication", "associatedTutorial",
		"associatedWordlist", "language" })
public class SmarterAppMetadata {
	private String identifier;
	private String itemAuthorIdentifier;
	private String itemSpecFormat;
	private String lastModifiedBy;
	private String securityStatus;
	private String smarterAppItemDescriptor;
	private String status;
	private String stimulusFormat;
	private String subject;
	private String version;
	private String intendedGrade;
	private String minimumGrade;
	private String maximumGrade;
	private String depthOfKnowledge;
	private String interactionType;
	private String maximumNumberOfPoints;
	private String allowCalculator;
	private String copyrightAndOtherRestrictions;
	private String brailleType;
	private String enemyItem;
	private StandardPublication StandardPublication;
	private String associatedTutorial;
	private String associatedWordlist;
	private String language;

	@XmlElement(name = "Identifier")
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@XmlElement(name = "SmarterAppItemDescriptor")
	public String getSmarterAppItemDescriptor() {
		return smarterAppItemDescriptor;
	}

	public void setSmarterAppItemDescriptor(String smarterAppItemDescriptor) {
		this.smarterAppItemDescriptor = smarterAppItemDescriptor;
	}

	@XmlElement(name = "Subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@XmlElement(name = "Version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "MaximumGrade")
	public String getMaximumGrade() {
		return maximumGrade;
	}

	public void setMaximumGrade(String maximumGrade) {
		this.maximumGrade = maximumGrade;
	}

	@XmlElement(name = "StandardPublication")
	public StandardPublication getStandardPublication() {
		return StandardPublication;
	}

	public void setStandardPublication(StandardPublication standardPublication) {
		StandardPublication = standardPublication;
	}

	@XmlElement(name = "IntendedGrade")
	public String getIntendedGrade() {
		return intendedGrade;
	}

	public void setIntendedGrade(String intendedGrade) {
		this.intendedGrade = intendedGrade;
	}

	@XmlElement(name = "MinimumGrade")
	public String getMinimumGrade() {
		return minimumGrade;
	}

	public void setMinimumGrade(String minimumGrade) {
		this.minimumGrade = minimumGrade;
	}

	@XmlElement(name = "InteractionType")
	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

	@XmlElement(name = "ItemAuthorIdentifier")
	public String getItemAuthorIdentifier() {
		return itemAuthorIdentifier;
	}

	public void setItemAuthorIdentifier(String itemAuthorIdentifier) {
		this.itemAuthorIdentifier = itemAuthorIdentifier;
	}

	@XmlElement(name = "ItemSpecFormat")
	public String getItemSpecFormat() {
		return itemSpecFormat;
	}

	public void setItemSpecFormat(String itemSpecFormat) {
		this.itemSpecFormat = itemSpecFormat;
	}

	@XmlElement(name = "LastModifiedBy")
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@XmlElement(name = "SecurityStatus")
	public String getSecurityStatus() {
		return securityStatus;
	}

	public void setSecurityStatus(String securityStatus) {
		this.securityStatus = securityStatus;
	}

	@XmlElement(name = "Status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "StimulusFormat")
	public String getStimulusFormat() {
		return stimulusFormat;
	}

	public void setStimulusFormat(String stimulusFormat) {
		this.stimulusFormat = stimulusFormat;
	}

	@XmlElement(name = "DepthOfKnowledge")
	public String getDepthOfKnowledge() {
		return depthOfKnowledge;
	}

	public void setDepthOfKnowledge(String depthOfKnowledge) {
		this.depthOfKnowledge = depthOfKnowledge;
	}

	@XmlElement(name = "MaximumNumberOfPoints")
	public String getMaximumNumberOfPoints() {
		return maximumNumberOfPoints;
	}

	public void setMaximumNumberOfPoints(String maximumNumberOfPoints) {
		this.maximumNumberOfPoints = maximumNumberOfPoints;
	}

	@XmlElement(name = "AllowCalculator")
	public String getAllowCalculator() {
		return allowCalculator;
	}

	public void setAllowCalculator(String allowCalculator) {
		this.allowCalculator = allowCalculator;
	}

	@XmlElement(name = "CopyrightAndOtherRestrictions")
	public String getCopyrightAndOtherRestrictions() {
		return copyrightAndOtherRestrictions;
	}

	public void setCopyrightAndOtherRestrictions(String copyrightAndOtherRestrictions) {
		this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
	}

	@XmlElement(name = "BrailleType")
	public String getBrailleType() {
		return brailleType;
	}

	public void setBrailleType(String brailleType) {
		this.brailleType = brailleType;
	}

	@XmlElement(name = "EnemyItem")
	public String getEnemyItem() {
		return enemyItem;
	}

	public void setEnemyItem(String enemyItem) {
		this.enemyItem = enemyItem;
	}

	@XmlElement(name = "AssociatedTutorial")
	public String getAssociatedTutorial() {
		return associatedTutorial;
	}

	public void setAssociatedTutorial(String associatedTutorial) {
		this.associatedTutorial = associatedTutorial;
	}

	@XmlElement(name = "AssociatedWordlist")
	public String getAssociatedWordlist() {
		return associatedWordlist;
	}

	public void setAssociatedWordlist(String associatedWordlist) {
		this.associatedWordlist = associatedWordlist;
	}

	@XmlElement(name = "Language")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
