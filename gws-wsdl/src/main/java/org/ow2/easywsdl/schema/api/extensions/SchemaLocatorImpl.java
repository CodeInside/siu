package org.ow2.easywsdl.schema.api.extensions;

import java.util.HashMap;
import java.util.Map;

public class SchemaLocatorImpl {

	private Map<String,String> schemaLocations = new HashMap<String,String>();

	public void addSchemaLocation(String schemaUri, String location) {
		schemaLocations .put(schemaUri, location);
	}

	public Map<String, String> getSchemaLocations() {
		return schemaLocations;
	}
}
