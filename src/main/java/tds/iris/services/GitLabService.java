package tds.iris.services;

import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

import tds.iris.jo.Commit;
import tds.iris.jo.Project;

public class GitLabService {
	private String apiToken;
	private String basePath;
	
	public GitLabService() {
		apiToken = "wng4GQ5QiXVBE6kv1f_D";
		basePath = "http://164.67.228.46";
	}
	
	public List<Project> getProjects() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		try {
			
			ClientConfig clientConfig = new DefaultClientConfig();
			//clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			Client client = Client.create(clientConfig);
			client.addFilter(new LoggingFilter(System.out));
			

			WebResource resource = client.resource(basePath + "/api/v3/projects/all");

			ClientResponse response = resource.header("PRIVATE-TOKEN",  apiToken).accept("application/json").get(
					ClientResponse.class);

			if (response.getStatus() != 200)
				throw new Exception("Failed: HTTP error code: "
						+ response.getStatus());			
			
			projects = response.getEntity(new GenericType<List<Project>>() {});
			
			for(Project p : projects) {
				resource = client.resource(basePath + "/api/v3/projects/" + p.id + "/repository/commits");
				response = resource.header("PRIVATE-TOKEN", apiToken).accept("application/json").get(ClientResponse.class);
				p.commits = response.getEntity(new GenericType<List<Commit>>(){});
			}
			
		}
		catch(Exception e) {
			System.out.println("GitLabService: " + e.getMessage());
		}
		
		return projects;
	}

}
