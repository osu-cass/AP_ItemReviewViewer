package tds.iris.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import tds.iris.rest.ItemController;

public class ApiResourceConfig extends ResourceConfig {

	public ApiResourceConfig()
	{
		// Item Controller
		register(ItemController.class);
		
        // Jackson-Json Support
        register(JacksonFeature.class);
	}
}
