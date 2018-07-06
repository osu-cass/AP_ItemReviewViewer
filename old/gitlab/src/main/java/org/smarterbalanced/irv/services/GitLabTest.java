/**
 * 
 */
package org.smarterbalanced.irv.services;

import java.util.List;

import org.smarterbalanced.irv.model.ItemCommit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author kthotti
 *
 */
public class GitLabTest {

	/**
	 * 
	 */
	public GitLabTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		try {
			
			//ClientConfig clientConfig = new DefaultClientConfig();
	        //clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
	        Client client = Client.create();
	        
			
			String getListURL = "https://itembank.smarterbalanced.org/api/v3/projects/itemreviewapp%2FItem-187-3212/repository/commits?private_token=Gb_c4nLnUmKGy8VAWEy2";
			WebResource webResourceGet = client.resource(getListURL);
			ClientResponse response = webResourceGet.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			
			ObjectMapper objectMapper = new ObjectMapper();
			//List items = objectMapper.readValue(output, List.class);
			List<ItemCommit> items = objectMapper.readValue(output, new TypeReference<List<ItemCommit>>(){});			
			
			System.out.println("Output from Server .... \n");
			System.out.println(items);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
}
