package org.smarterbalanced.itemreviewviewer.web.services.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarterbalanced.itemreviewviewer.web.models.metadata.AboutItemMetadataModel;
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
