package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AboutItemMetadata {
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

    public AboutItemMetadata(String itemKey, String bankKey, String revision, String section){
        identifier = bankKey;
        itemAuthorIdentifier = "Hannah_Hacker";
        itemSpecFormat = "SmarterApp";
        lastModifiedBy = "Hannah_Hacker";
        securityStatus = "Non-secure";
        smarterAppItemDescriptor = "Describes item " + itemKey + "-" + bankKey;
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
        StandardPublication = new StandardPublication();
        StandardPublication.setPrimaryStandard("SBAC-SH-v1:SH-Undesignated<");
        StandardPublication.setPublication("SBAC-SH-v1");
        associatedTutorial = "NA";
        associatedWordlist = "NA";
        language = "english";
    }

    @JsonProperty("Identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("SmarterAppItemDescriptor")
    public String getSmarterAppItemDescriptor() {
        return smarterAppItemDescriptor;
    }

    public void setSmarterAppItemDescriptor(String smarterAppItemDescriptor) {
        this.smarterAppItemDescriptor = smarterAppItemDescriptor;
    }

    @JsonProperty("Subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("Version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("MaximumGrade")
    public String getMaximumGrade() {
        return maximumGrade;
    }

    public void setMaximumGrade(String maximumGrade) {
        this.maximumGrade = maximumGrade;
    }

    @JsonProperty("StandardPublication")
    public StandardPublication getStandardPublication() {
        return StandardPublication;
    }

    public void setStandardPublication(StandardPublication standardPublication) {
        StandardPublication = standardPublication;
    }

    @JsonProperty("IntendedGrade")
    public String getIntendedGrade() {
        return intendedGrade;
    }

    public void setIntendedGrade(String intendedGrade) {
        this.intendedGrade = intendedGrade;
    }

    @JsonProperty("MinimumGrade")
    public String getMinimumGrade() {
        return minimumGrade;
    }

    public void setMinimumGrade(String minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    @JsonProperty("InteractionType")
    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    @JsonProperty("ItemAuthorIdentifier")
    public String getItemAuthorIdentifier() {
        return itemAuthorIdentifier;
    }

    public void setItemAuthorIdentifier(String itemAuthorIdentifier) {
        this.itemAuthorIdentifier = itemAuthorIdentifier;
    }

    @JsonProperty("ItemSpecFormat")
    public String getItemSpecFormat() {
        return itemSpecFormat;
    }

    public void setItemSpecFormat(String itemSpecFormat) {
        this.itemSpecFormat = itemSpecFormat;
    }

    @JsonProperty("LastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonProperty("SecurityStatus")
    public String getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(String securityStatus) {
        this.securityStatus = securityStatus;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("StimulusFormat")
    public String getStimulusFormat() {
        return stimulusFormat;
    }

    public void setStimulusFormat(String stimulusFormat) {
        this.stimulusFormat = stimulusFormat;
    }

    @JsonProperty("DepthOfKnowledge")
    public String getDepthOfKnowledge() {
        return depthOfKnowledge;
    }

    public void setDepthOfKnowledge(String depthOfKnowledge) {
        this.depthOfKnowledge = depthOfKnowledge;
    }

    @JsonProperty("MaximumNumberOfPoints")
    public String getMaximumNumberOfPoints() {
        return maximumNumberOfPoints;
    }

    public void setMaximumNumberOfPoints(String maximumNumberOfPoints) {
        this.maximumNumberOfPoints = maximumNumberOfPoints;
    }

    @JsonProperty("AllowCalculator")
    public String getAllowCalculator() {
        return allowCalculator;
    }

    public void setAllowCalculator(String allowCalculator) {
        this.allowCalculator = allowCalculator;
    }

    @JsonProperty("CopyrightAndOtherRestrictions")
    public String getCopyrightAndOtherRestrictions() {
        return copyrightAndOtherRestrictions;
    }

    public void setCopyrightAndOtherRestrictions(String copyrightAndOtherRestrictions) {
        this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
    }

    @JsonProperty("BrailleType")
    public String getBrailleType() {
        return brailleType;
    }

    public void setBrailleType(String brailleType) {
        this.brailleType = brailleType;
    }

    @JsonProperty("EnemyItem")
    public String getEnemyItem() {
        return enemyItem;
    }

    public void setEnemyItem(String enemyItem) {
        this.enemyItem = enemyItem;
    }

    @JsonProperty("AssociatedTutorial")
    public String getAssociatedTutorial() {
        return associatedTutorial;
    }

    public void setAssociatedTutorial(String associatedTutorial) {
        this.associatedTutorial = associatedTutorial;
    }

    @JsonProperty("AssociatedWordlist")
    public String getAssociatedWordlist() {
        return associatedWordlist;
    }

    public void setAssociatedWordlist(String associatedWordlist) {
        this.associatedWordlist = associatedWordlist;
    }

    @JsonProperty("Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
