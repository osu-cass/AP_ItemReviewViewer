package tds.iris.jo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement(name="Project")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Project {
	@XmlElement(name="id")
	public int id;
	@XmlElement(name="description")
	public String description;
	@XmlElement(name="name")
	public String name;
	@XmlElement(name="created_at")
	public String created_at;
	public List<Commit> commits;
}
