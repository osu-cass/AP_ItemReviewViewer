package tds.iris.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="metadata")
public class MetaData {
	
	public MetaData() {
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
