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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.saiku.reporting.core.model.types.GroupType;
import org.saiku.reporting.core.model.types.SortType;

@XmlRootElement(name="group")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupDefinition {
	
	public GroupDefinition() {

		this.footerFormat = new RootBandFormat();
		this.headerFormats = new ArrayList<RootBandFormat>();
		this.sort = SortType.ASC; //brauch ich das hier im modell? ist doch eigtl. in der query
		this.type = GroupType.RELATIONAL;
		this.printSummary = false;
	}

	public boolean isPrintSummary() {
		return printSummary;
	}

	public void setPrintSummary(boolean printSummary) {
		this.printSummary = printSummary;
	}

	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getNullString() {
		return nullString;
	}

	public void setNullString(String nullString) {
		this.nullString = nullString;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String id) {
		this.fieldId = id;
	}

	public SortType getSort() {
		return sort;
	}

	public void setSort(SortType sort) {
		this.sort = sort;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setHeaderFormats(ArrayList<RootBandFormat> headerFormats) {
		this.headerFormats = headerFormats;
	}

	public List<RootBandFormat> getHeaderFormats() {
		return headerFormats;
	}

	public void setFooterFormat(RootBandFormat footerFormat) {
		this.footerFormat = footerFormat;
	}

	public RootBandFormat getFooterFormat() {
		return footerFormat;
	}

	@XmlElement(name="type")
	@JsonProperty("type")
	private GroupType type;
	
	@XmlElement(name="data-format")
	@JsonProperty("dataFormat")
	private String dataFormat;
	
	@XmlElement(name="null-string")
	@JsonProperty("nullString")
	private String nullString;
	
	@XmlElement(name="display-name")
	@JsonProperty("displayName")
	private String displayName;
	
	@XmlElement(name="field-id")
	@JsonProperty("fieldId")
	private String fieldId;
	
	@XmlElement(name="group-sort")
	@JsonProperty("groupSort")
	private SortType sort;

	@XmlElement(name="group-name")
	@JsonProperty("groupName")
	private String groupName;

	@XmlElement(name="print-summary")
	@JsonProperty("printSummary")
	private boolean printSummary;
	
	@XmlElement(name="header-formats")
	@JsonProperty("headerFormats")
	
    /*
    On relational groupheader only 1) is used
    on Crosstab we have:
    1) Chrosstab Header
    2) Title Header
    3) Summary Header
     */
	private ArrayList<RootBandFormat> headerFormats;

	@XmlElement(name="footer-format")
	@JsonProperty("footerFormat")
	private RootBandFormat footerFormat;
	
}
