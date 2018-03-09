package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public AboutItemMetadataModel(String itemKey, String bankKey, String revision, String section){
        identifier = itemKey;
        itemAuthorIdentifier = "Hannah_Hacker";
        itemSpecFormat = "SmarterApp";
        lastModifiedBy = "Hannah_Hacker";
        securityStatus = "Non-secure";
        smarterAppItemDescriptor = "Describes item " + bankKey + "-" + itemKey;
        status = "Released";
        stimulusFormat = "Standard";
        subject = "subject type";
        version = revision;
        intendedGrade = "NA";
        minimumGrade = "NA";
        maximumGrade = "NA";
        interactionType = section;
        maximumNumberOfPoints = String.valueOf((int) 10 * (Math.random() % 10));
        allowCalculator = "false";
        copyrightAndOtherRestrictions = "NA";
        brailleType = "NA";
        enemyItem = "NA";
        standardPublication = new StandardPublication();
        standardPublication.setPrimaryStandard("SBAC-SH-v1:SH-Undesignated<");
        standardPublication.setPublication("SBAC-SH-v1");
        associatedTutorial = "NA";
        associatedWordlist = "NA";
        language = "english";
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("smarterAppItemDescriptor")
    public String getSmarterAppItemDescriptor() {
        return smarterAppItemDescriptor;
    }

    public void setSmarterAppItemDescriptor(String smarterAppItemDescriptor) {
        this.smarterAppItemDescriptor = smarterAppItemDescriptor;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("maximumGrade")
    public String getMaximumGrade() {
        return maximumGrade;
    }

    public void setMaximumGrade(String maximumGrade) {
        this.maximumGrade = maximumGrade;
    }

    @JsonProperty("standardPublication")
    public StandardPublication getStandardPublication() {
        return standardPublication;
    }

    public void setStandardPublication(StandardPublication standardPublication) {
        this.standardPublication = standardPublication;
    }

    @JsonProperty("intendedGrade")
    public String getIntendedGrade() {
        return intendedGrade;
    }

    public void setIntendedGrade(String intendedGrade) {
        this.intendedGrade = intendedGrade;
    }

    @JsonProperty("minimumGrade")
    public String getMinimumGrade() {
        return minimumGrade;
    }

    public void setMinimumGrade(String minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    @JsonProperty("interactionType")
    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    @JsonProperty("itemAuthorIdentifier")
    public String getItemAuthorIdentifier() {
        return itemAuthorIdentifier;
    }

    public void setItemAuthorIdentifier(String itemAuthorIdentifier) {
        this.itemAuthorIdentifier = itemAuthorIdentifier;
    }

    @JsonProperty("itemSpecFormat")
    public String getItemSpecFormat() {
        return itemSpecFormat;
    }

    public void setItemSpecFormat(String itemSpecFormat) {
        this.itemSpecFormat = itemSpecFormat;
    }

    @JsonProperty("lastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonProperty("securityStatus")
    public String getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(String securityStatus) {
        this.securityStatus = securityStatus;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("stimulusFormat")
    public String getStimulusFormat() {
        return stimulusFormat;
    }

    public void setStimulusFormat(String stimulusFormat) {
        this.stimulusFormat = stimulusFormat;
    }

    @JsonProperty("depthOfKnowledge")
    public String getDepthOfKnowledge() {
        return depthOfKnowledge;
    }

    public void setDepthOfKnowledge(String depthOfKnowledge) {
        this.depthOfKnowledge = depthOfKnowledge;
    }

    @JsonProperty("maximumNumberOfPoints")
    public String getMaximumNumberOfPoints() {
        return maximumNumberOfPoints;
    }

    public void setMaximumNumberOfPoints(String maximumNumberOfPoints) {
        this.maximumNumberOfPoints = maximumNumberOfPoints;
    }

    @JsonProperty("allowCalculator")
    public String getAllowCalculator() {
        return allowCalculator;
    }

    public void setAllowCalculator(String allowCalculator) {
        this.allowCalculator = allowCalculator;
    }

    @JsonProperty("copyrightAndOtherRestrictions")
    public String getCopyrightAndOtherRestrictions() {
        return copyrightAndOtherRestrictions;
    }

    public void setCopyrightAndOtherRestrictions(String copyrightAndOtherRestrictions) {
        this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
    }

    @JsonProperty("brailleType")
    public String getBrailleType() {
        return brailleType;
    }

    public void setBrailleType(String brailleType) {
        this.brailleType = brailleType;
    }

    @JsonProperty("enemyItem")
    public String getEnemyItem() {
        return enemyItem;
    }

    public void setEnemyItem(String enemyItem) {
        this.enemyItem = enemyItem;
    }

    @JsonProperty("associatedTutorial")
    public String getAssociatedTutorial() {
        return associatedTutorial;
    }

    public void setAssociatedTutorial(String associatedTutorial) {
        this.associatedTutorial = associatedTutorial;
    }

    @JsonProperty("associatedWordlist")
    public String getAssociatedWordlist() {
        return associatedWordlist;
    }

    public void setAssociatedWordlist(String associatedWordlist) {
        this.associatedWordlist = associatedWordlist;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
