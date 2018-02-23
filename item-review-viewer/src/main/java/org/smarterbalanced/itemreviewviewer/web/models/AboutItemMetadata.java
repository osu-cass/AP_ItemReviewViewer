package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty(name = "Identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty(name = "SmarterAppItemDescriptor")
    public String getSmarterAppItemDescriptor() {
        return smarterAppItemDescriptor;
    }

    public void setSmarterAppItemDescriptor(String smarterAppItemDescriptor) {
        this.smarterAppItemDescriptor = smarterAppItemDescriptor;
    }

    @JsonProperty(name = "Subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty(name = "Version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty(name = "MaximumGrade")
    public String getMaximumGrade() {
        return maximumGrade;
    }

    public void setMaximumGrade(String maximumGrade) {
        this.maximumGrade = maximumGrade;
    }

    @JsonProperty(name = "StandardPublication")
    public StandardPublication getStandardPublication() {
        return StandardPublication;
    }

    public void setStandardPublication(StandardPublication standardPublication) {
        StandardPublication = standardPublication;
    }

    @JsonProperty(name = "IntendedGrade")
    public String getIntendedGrade() {
        return intendedGrade;
    }

    public void setIntendedGrade(String intendedGrade) {
        this.intendedGrade = intendedGrade;
    }

    @JsonProperty(name = "MinimumGrade")
    public String getMinimumGrade() {
        return minimumGrade;
    }

    public void setMinimumGrade(String minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    @JsonProperty(name = "InteractionType")
    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    @JsonProperty(name = "ItemAuthorIdentifier")
    public String getItemAuthorIdentifier() {
        return itemAuthorIdentifier;
    }

    public void setItemAuthorIdentifier(String itemAuthorIdentifier) {
        this.itemAuthorIdentifier = itemAuthorIdentifier;
    }

    @JsonProperty(name = "ItemSpecFormat")
    public String getItemSpecFormat() {
        return itemSpecFormat;
    }

    public void setItemSpecFormat(String itemSpecFormat) {
        this.itemSpecFormat = itemSpecFormat;
    }

    @JsonProperty(name = "LastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonProperty(name = "SecurityStatus")
    public String getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(String securityStatus) {
        this.securityStatus = securityStatus;
    }

    @JsonProperty(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty(name = "StimulusFormat")
    public String getStimulusFormat() {
        return stimulusFormat;
    }

    public void setStimulusFormat(String stimulusFormat) {
        this.stimulusFormat = stimulusFormat;
    }

    @JsonProperty(name = "DepthOfKnowledge")
    public String getDepthOfKnowledge() {
        return depthOfKnowledge;
    }

    public void setDepthOfKnowledge(String depthOfKnowledge) {
        this.depthOfKnowledge = depthOfKnowledge;
    }

    @JsonProperty(name = "MaximumNumberOfPoints")
    public String getMaximumNumberOfPoints() {
        return maximumNumberOfPoints;
    }

    public void setMaximumNumberOfPoints(String maximumNumberOfPoints) {
        this.maximumNumberOfPoints = maximumNumberOfPoints;
    }

    @JsonProperty(name = "AllowCalculator")
    public String getAllowCalculator() {
        return allowCalculator;
    }

    public void setAllowCalculator(String allowCalculator) {
        this.allowCalculator = allowCalculator;
    }

    @JsonProperty(name = "CopyrightAndOtherRestrictions")
    public String getCopyrightAndOtherRestrictions() {
        return copyrightAndOtherRestrictions;
    }

    public void setCopyrightAndOtherRestrictions(String copyrightAndOtherRestrictions) {
        this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
    }

    @JsonProperty(name = "BrailleType")
    public String getBrailleType() {
        return brailleType;
    }

    public void setBrailleType(String brailleType) {
        this.brailleType = brailleType;
    }

    @JsonProperty(name = "EnemyItem")
    public String getEnemyItem() {
        return enemyItem;
    }

    public void setEnemyItem(String enemyItem) {
        this.enemyItem = enemyItem;
    }

    @JsonProperty(name = "AssociatedTutorial")
    public String getAssociatedTutorial() {
        return associatedTutorial;
    }

    public void setAssociatedTutorial(String associatedTutorial) {
        this.associatedTutorial = associatedTutorial;
    }

    @JsonProperty(name = "AssociatedWordlist")
    public String getAssociatedWordlist() {
        return associatedWordlist;
    }

    public void setAssociatedWordlist(String associatedWordlist) {
        this.associatedWordlist = associatedWordlist;
    }

    @JsonProperty(name = "Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
