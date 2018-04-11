package org.smarterbalanced.itemreviewviewer.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="metadata")
public class Metadata {

    AboutItemMetadataModel smarterAppMetadata;

    @JsonProperty("AboutItemMetadata")
    @XmlElement(name="smarterAppMetadata")
    public AboutItemMetadataModel getSmarterAppMetadata() {
        return smarterAppMetadata;
    }

    public void setSmarterAppMetadata(AboutItemMetadataModel smarterAppMetadata) {
        this.smarterAppMetadata = smarterAppMetadata;
    }
}
