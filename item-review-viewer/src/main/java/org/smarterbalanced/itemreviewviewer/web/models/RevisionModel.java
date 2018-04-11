package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class RevisionModel {
    private String author;
    private long date;
    private String commitMessage;
    private String commitHash;
    private boolean selected;


    public RevisionModel(String author, String commitMessage, String commitHash, boolean selected){
        this.author = author;
        this.date = new DateTime(DateTimeZone.UTC).getMillis();
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
