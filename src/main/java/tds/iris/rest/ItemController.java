package tds.iris.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tds.iris.jo.Project;
import tds.iris.services.GitLabService;

//@Path("/item")
@Component
@Scope("singleton")
public class ItemController {

	@GET
	@Path("/")
	@Produces("application/json")
	public Response getAll() {
		GitLabService service = new GitLabService();
		try {
			GenericEntity<List<Project>> entity = new GenericEntity<List<Project>>(service.getProjects()) {};
			
			return Response.status(200).entity(entity).build();
		} catch (Exception e) {

		}
		return Response.status(500).build();
	}

	
	public Response getItem(String itemName) {
		//URI targetUri = URI.create("http://164.67.228.46/api/v3/projects/root%2F" + itemName + "/repository/archive.zip?private_token=wng4GQ5QiXVBE6kv1f_D");
		try{
			URI targetUri = URI.create("http://ba-apps-t01.it.ucla.edu:9080/api/v3/projects/root%2F" + itemName + "/repository/archive.zip?private_token=i_iU8XySyymdvUQQFdHD");
			return Response.temporaryRedirect(targetUri).build();
		}catch(Exception e){
			e.printStackTrace();
			return Response.serverError().build();
		}
		
	}
	
	@GET
	@Path("/{id}/{sha}")
	public Response getItemCommit(@PathParam("id") String itemName, @PathParam("sha") String commitSha) {
		URI targetUri = URI.create("http://ba-apps-t01.it.ucla.edu:9080/api/v3/projects/root%2F" + itemName + "/repository/archive.zip?private_token=i_iU8XySyymdvUQQFdHD&sha=" + commitSha);
		return Response.temporaryRedirect(targetUri).build();
	}
}
