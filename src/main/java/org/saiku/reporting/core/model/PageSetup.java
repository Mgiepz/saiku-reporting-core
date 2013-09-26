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

@XmlRootElement(name="page-setup")
@XmlAccessorType(XmlAccessType.FIELD)
public class PageSetup {
	
	@XmlElement(name="page-orientation")
	@JsonProperty("pageOrientation")
	private int pageOrientation;
	
	@XmlElement(name="page-format")
	@JsonProperty("pageFormat")
	private String pageFormat;
	
	@XmlElement(name="top-margin")
	@JsonProperty("topMargin")
	private Integer topMargin;
	
	@XmlElement(name="right-margin")
	@JsonProperty("rightMargin")
	private Integer rightMargin;
	
	@XmlElement(name="bottom-margin")
	@JsonProperty("bottomMargin")
	private Integer bottomMargin;
	
	@XmlElement(name="left-margin")
	@JsonProperty("leftMargin")
	private Integer leftMargin;

	public Integer getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(int pageOrientation) {
		this.pageOrientation = pageOrientation;
	}

	public String getPageFormat() {
		return pageFormat;
	}

	public void setPageFormat(String pageFormat) {
		this.pageFormat = pageFormat;
	}

	public Integer getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(Integer topMargin) {
		this.topMargin = topMargin;
	}

	public Integer getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(Integer rightMargin) {
		this.rightMargin = rightMargin;
	}

	public Integer getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(Integer bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public Integer getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(Integer leftMargin) {
		this.leftMargin = leftMargin;
	}

}
