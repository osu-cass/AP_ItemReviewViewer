package tds.iris.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlRootElement(name="smarterAppMetadata")
@XmlType(propOrder={"identifier", "smarterAppItemDescriptor", "subject", "version","intendedGrade", "minimumGrade", "maximumGrade", "interactionType","standardPublication"})
public class SmarterAppMetadata {
	private String identifier;
	private String smarterAppItemDescriptor;
	private String subject;
	private String version;
	private String intendedGrade;
	private String minimumGrade;
	private String maximumGrade;
	private String interactionType;
	private StandardPublication StandardPublication;
	
	@XmlElement(name = "Identifier")
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	@XmlElement(name="SmarterAppItemDescriptor")
	public String getSmarterAppItemDescriptor() {
		return smarterAppItemDescriptor;
	}
	public void setSmarterAppItemDescriptor(String smarterAppItemDescriptor) {
		this.smarterAppItemDescriptor = smarterAppItemDescriptor;
	}
	@XmlElement(name="Subject")
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@XmlElement(name="Version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@XmlElement(name="MaximumGrade")
	public String getMaximumGrade() {
		return maximumGrade;
	}
	public void setMaximumGrade(String maximumGrade) {
		this.maximumGrade = maximumGrade;
	}
	
	@XmlElement(name="StandardPublication")
	public StandardPublication getStandardPublication() {
		return StandardPublication;
	}
	public void setStandardPublication(StandardPublication standardPublication) {
		StandardPublication = standardPublication;
	}
	@XmlElement(name="IntendedGrade")
	public String getIntendedGrade() {
		return intendedGrade;
	}
	public void setIntendedGrade(String intendedGrade) {
		this.intendedGrade = intendedGrade;
	}
	@XmlElement(name="MinimumGrade")
	public String getMinimumGrade() {
		return minimumGrade;
	}
	public void setMinimumGrade(String minimumGrade) {
		this.minimumGrade = minimumGrade;
	}
	@XmlElement(name="InteractionType")
	public String getInteractionType() {
		return interactionType;
	}
	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

}
