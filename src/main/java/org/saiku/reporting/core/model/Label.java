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

/*
 * Label is in fact a message field
 */
@XmlRootElement(name="text-label")
@XmlAccessorType(XmlAccessType.FIELD)
public class Label {
	
	@XmlElement(name="format")
	@JsonProperty("format")
	private ElementFormat format;

	/*
	 * value can be a text or a message-string
	 */
	@XmlElement(name="value")
	@JsonProperty("value")
	private String value;

	
	public Label(String string) {
		value = string;
	}

	public Label() {
		super();
	}

	public ElementFormat getFormat() {
		return format;
	}

	public void setFormat(ElementFormat format) {
		this.format = format;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
