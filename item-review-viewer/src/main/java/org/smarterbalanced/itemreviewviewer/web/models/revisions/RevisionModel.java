package org.smarterbalanced.itemreviewviewer.web.models.revisions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RevisionModel {
    private String author;
    private long date;
    private String commitMessage;
    private String commitHash;
    private boolean selected;


    public RevisionModel(String author, long date, String commitMessage, String commitHash, boolean selected){
        this.author = author;
        this.date = date;
        this.commitMessage = commitMessage;
        this.commitHash = commitHash;
        this.selected = selected;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("date")
    public long getDate() {
        return date;
    }


    @JsonProperty("commitMessage")
    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    @JsonProperty("commitHash")
    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    @JsonProperty("selected")
    public boolean getSelected(){
        return this.selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
