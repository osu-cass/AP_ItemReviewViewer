package org.smarterbalanced.itemreviewviewer.web.models.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "smarterAppMetadata")
@XmlType(propOrder = { "identifier", "claim", "itemAuthorIdentifier", "itemSpecFormat", "lastModifiedBy", "securityStatus",
        "smarterAppItemDescriptor", "status", "stimulusFormat", "subject", "version", "intendedGrade", "minimumGrade",
        "maximumGrade", "depthOfKnowledge", "interactionType", "maximumNumberOfPoints", "allowCalculator",
        "copyrightAndOtherRestrictions", "brailleType", "enemyItem", "standardPublication", "associatedTutorial",
        "associatedWordlist", "language" })
public class AboutItemMetadataModel {
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
    private StandardPublication standardPublication;
    private String associatedTutorial;
    private String associatedWordlist;
    private String language;
    private String claim;

    @XmlElement(name = "Identifier")
    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @XmlElement(name = "Claim")
    @JsonProperty("claim")
    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    @XmlElement(name = "SmarterAppItemDescriptor")
    @JsonProperty("smarterAppItemDescriptor")
    public String getSmarterAppItemDescriptor() {
        return smarterAppItemDescriptor;
    }

    public void setSmarterAppItemDescriptor(String smarterAppItemDescriptor) {
        this.smarterAppItemDescriptor = smarterAppItemDescriptor;
    }

    @XmlElement(name = "Subject")
    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @XmlElement(name = "Version")
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElement(name = "MaximumGrade")
    @JsonProperty("maximumGrade")
    public String getMaximumGrade() {
        return maximumGrade;
    }

    public void setMaximumGrade(String maximumGrade) {
        this.maximumGrade = maximumGrade;
    }

    @XmlElement(name = "StandardPublication")
    @JsonProperty("standardPublication")
    public StandardPublication getStandardPublication() {
        return standardPublication;
    }

    public void setStandardPublication(StandardPublication standardPublication) {
        this.standardPublication = standardPublication;
    }

    @XmlElement(name = "IntendedGrade")
    @JsonProperty("intendedGrade")
    public String getIntendedGrade() {
        return intendedGrade;
    }

    public void setIntendedGrade(String intendedGrade) {
        this.intendedGrade = intendedGrade;
    }

    @XmlElement(name = "MinimumGrade")
    @JsonProperty("minimumGrade")
    public String getMinimumGrade() {
        return minimumGrade;
    }

    public void setMinimumGrade(String minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    @XmlElement(name = "InteractionType")
    @JsonProperty("interactionType")
    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    @XmlElement(name = "ItemAuthorIdentifier")
    @JsonProperty("itemAuthorIdentifier")
    public String getItemAuthorIdentifier() {
        return itemAuthorIdentifier;
    }

    public void setItemAuthorIdentifier(String itemAuthorIdentifier) {
        this.itemAuthorIdentifier = itemAuthorIdentifier;
    }

    @XmlElement(name = "ItemSpecFormat")
    @JsonProperty("itemSpecFormat")
    public String getItemSpecFormat() {
        return itemSpecFormat;
    }

    public void setItemSpecFormat(String itemSpecFormat) {
        this.itemSpecFormat = itemSpecFormat;
    }

    @XmlElement(name = "LastModifiedBy")
    @JsonProperty("lastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @XmlElement(name = "SecurityStatus")
    @JsonProperty("securityStatus")
    public String getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(String securityStatus) {
        this.securityStatus = securityStatus;
    }

    @XmlElement(name = "Status")
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(name = "StimulusFormat")
    @JsonProperty("stimulusFormat")
    public String getStimulusFormat() {
        return stimulusFormat;
    }

    public void setStimulusFormat(String stimulusFormat) {
        this.stimulusFormat = stimulusFormat;
    }

    @XmlElement(name = "DepthOfKnowledge")
    @JsonProperty("depthOfKnowledge")
    public String getDepthOfKnowledge() {
        return depthOfKnowledge;
    }

    public void setDepthOfKnowledge(String depthOfKnowledge) {
        this.depthOfKnowledge = depthOfKnowledge;
    }

    @XmlElement(name = "MaximumNumberOfPoints")
    @JsonProperty("maximumNumberOfPoints")
    public String getMaximumNumberOfPoints() {
        return maximumNumberOfPoints;
    }

    public void setMaximumNumberOfPoints(String maximumNumberOfPoints) {
        this.maximumNumberOfPoints = maximumNumberOfPoints;
    }

    @XmlElement(name = "AllowCalculator")
    @JsonProperty("allowCalculator")
    public String getAllowCalculator() {
        return allowCalculator;
    }

    public void setAllowCalculator(String allowCalculator) {
        this.allowCalculator = allowCalculator;
    }

    @XmlElement(name = "CopyrightAndOtherRestrictions")
    @JsonProperty("copyrightAndOtherRestrictions")
    public String getCopyrightAndOtherRestrictions() {
        return copyrightAndOtherRestrictions;
    }

    public void setCopyrightAndOtherRestrictions(String copyrightAndOtherRestrictions) {
        this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
    }

    @XmlElement(name = "BrailleType")
    @JsonProperty("brailleType")
    public String getBrailleType() {
        return brailleType;
    }

    public void setBrailleType(String brailleType) {
        this.brailleType = brailleType;
    }

    @XmlElement(name = "EnemyItem")
    @JsonProperty("enemyItem")
    public String getEnemyItem() {
        return enemyItem;
    }

    public void setEnemyItem(String enemyItem) {
        this.enemyItem = enemyItem;
    }

    @XmlElement(name = "AssociatedTutorial")
    @JsonProperty("associatedTutorial")
    public String getAssociatedTutorial() {
        return associatedTutorial;
    }

    public void setAssociatedTutorial(String associatedTutorial) {
        this.associatedTutorial = associatedTutorial;
    }

    @XmlElement(name = "AssociatedWordlist")
    @JsonProperty("associatedWordlist")
    public String getAssociatedWordlist() {
        return associatedWordlist;
    }

    public void setAssociatedWordlist(String associatedWordlist) {
        this.associatedWordlist = associatedWordlist;
    }

    @XmlElement(name = "Language", required = true)
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
