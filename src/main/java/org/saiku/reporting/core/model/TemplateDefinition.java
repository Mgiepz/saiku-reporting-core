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

@XmlRootElement(name="template-definition")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateDefinition {
	
	public TemplateDefinition(String url, String name) {	
		super();
		this.setUrl(url);
		this.setName(name);
	}
	
	public TemplateDefinition() {
	     this.url = new String();
	     this.name = new String();
	     this.description = new String();
	     this.displayName = new String();
	 }
	
	
	@XmlElement(name="url")	
	private String url;
	
	@XmlElement(name="name")	
	private String name;
	
	@XmlElement(name="displayName")	
	private String displayName;
	
	@XmlElement(name="description")	
	private String description;
	 
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
