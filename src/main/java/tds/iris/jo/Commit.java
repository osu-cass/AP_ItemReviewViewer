package tds.iris.jo;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement(name="Commit")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Commit {
	
	public String id;
	public String title;
	public String author_name;
	public String author_email;
	public String created_at;

}
