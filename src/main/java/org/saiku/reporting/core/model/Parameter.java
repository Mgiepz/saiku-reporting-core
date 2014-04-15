/*******************************************************************************
 * Copyright 2013 Marius Giepz and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.saiku.reporting.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name="parameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Parameter {
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getPrompt() {
		return prompt;
	}

	public void setPrompt(Boolean prompt) {
		this.prompt = prompt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DataSource getDataSourceId() {
		return dataSource;
	}

	public void setDataSourceId(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@XmlElement(name="id")
	@JsonProperty("id")
	private String id;

	@XmlElement(name="prompt")
	@JsonProperty("prompt")
	private Boolean prompt;	
	
	@XmlElement(name="name")
	@JsonProperty("name")
	private String name;
	
	@XmlElement(name="label")
	@JsonProperty("label")
	private String label;	
	
	//TODO: change that!
	@XmlElement(name="type")
	@JsonProperty("type")
	private String type;
	
	@XmlElement(name="dataSource")
	@JsonProperty("dataSource")
	private DataSource dataSource;

	@XmlElement(name="defaultValue")
	@JsonProperty("defaultValue")
	private String defaultValue;
	
}
