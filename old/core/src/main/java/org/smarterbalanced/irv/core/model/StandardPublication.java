package org.smarterbalanced.irv.core.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "StandardPublication")
@XmlType(propOrder = { "publication", "primaryStandard" })
public class StandardPublication {

	private String publication;
	private String primaryStandard;

	@XmlElement(name = "Publication")
	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	@XmlElement(name = "PrimaryStandard")
	public String getPrimaryStandard() {
		return primaryStandard;
	}

	public void setPrimaryStandard(String primaryStandard) {
		this.primaryStandard = primaryStandard;
	}

}
