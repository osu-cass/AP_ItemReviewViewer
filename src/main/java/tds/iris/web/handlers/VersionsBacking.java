/**
 * 
 */
package tds.iris.web.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kthotti
 *
 */
public class VersionsBacking {
	
	private List<String> versions;

	/**
	 * 
	 */
	public VersionsBacking() {
		// TODO Auto-generated constructor stub
		
	    versions = new ArrayList<String>();
	    versions.add("http://localhost:8090/iris/IrisPages/sample.xhtml?type=Item&bankId=187&id=1167&version=1234");
	    versions.add("http://localhost:8090/iris/IrisPages/sample.xhtml?type=Item&bankId=187&id=174&version=1234");
	    versions.add("http://localhost:8090/iris/IrisPages/sample.xhtml?type=Item&bankId=187&id=1059&version=1234");
	    versions.add("http://localhost:8090/iris/IrisPages/sample.xhtml?type=Item&bankId=187&id=540&version=1234");

	}

	public List<String> getVersions() {
		return versions;
	}

	public void setVersions(List<String> versions) {
		this.versions = versions;
	}
	
	public void addLink(String link) {
		versions.add(link);
	}
	
	

}
