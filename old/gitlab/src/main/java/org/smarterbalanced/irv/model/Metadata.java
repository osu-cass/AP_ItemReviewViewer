package org.smarterbalanced.irv.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="metadata")
public class Metadata {
	
	public Metadata() {
	}
	
	SmarterAppMetadata smarterAppMetadata;
	@XmlElement(name="smarterAppMetadata")
	public SmarterAppMetadata getSmarterAppMetadata() {
		return smarterAppMetadata;
	}

	public void setSmarterAppMetadata(SmarterAppMetadata smarterAppMetadata) {
		this.smarterAppMetadata = smarterAppMetadata;
	}

	    
	   
	
}
