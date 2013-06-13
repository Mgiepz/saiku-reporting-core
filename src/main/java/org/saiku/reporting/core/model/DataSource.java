package org.saiku.reporting.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.saiku.reporting.core.model.types.DatasourceType;

@XmlRootElement(name="datasource")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource {
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DatasourceType getType() {
		return type;
	}

	public void setType(DatasourceType type) {
		this.type = type;
	}

//	public HashMap<String, String> getProperties() {
//		return properties;
//	}
//
//	public void setProperties(HashMap<String, String> properties) {
//		this.properties = properties;
//	}

	@XmlElement(name="id")
	@JsonProperty("id")
	private String id;

	@XmlElement(name="type")
	@JsonProperty("type")
	private DatasourceType type;
	
	@XmlElement(name="query-string")
	@JsonProperty("queryString")
	private String queryString;

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}	

//	@XmlElement(name="properties")
//	//@XmlElementWrapper(name="properties")
//	@XmlJavaTypeAdapter(MapAdapter.class)
//	@JsonProperty("properties")
//	private HashMap<String,String> properties;
	
}
