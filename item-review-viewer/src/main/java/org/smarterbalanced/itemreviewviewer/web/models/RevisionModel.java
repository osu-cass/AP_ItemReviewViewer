package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class RevisionModel {
    protected String author;
    protected long date;
    protected String commitMessage;
    protected String commitHash;


    public RevisionModel(String author, String commitMessage, String commitHash){
        this.author = author;
        this.date = new DateTime(DateTimeZone.UTC).getMillis();
        this.commitMessage = commitMessage;
        this.commitHash = commitHash;
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
}
