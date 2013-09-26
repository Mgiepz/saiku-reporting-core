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
//package org.saiku.reporting.core.model;
//
//import java.util.ArrayList;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.codehaus.jackson.annotate.JsonProperty;
//
////@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
//@XmlRootElement(name="table-layout")
//@XmlAccessorType(XmlAccessType.FIELD)
//public class TableDefinition {
//
//	public TableDefinition() {
//		this.groupDefinitions = new ArrayList<GroupDefinition>();
//		this.columns = new ArrayList<FieldDefinition>();
//		this.detailsFooterBand = new RootBandFormat();
//		this.detailsFooterBand.setVisible(false);
//		this.detailsHeaderBand = new RootBandFormat();
//		
//		//TODO: do we need summary band here?
//		
//	}
//	
//	@XmlElementWrapper(name="groupDefinitions")
//	@XmlElement(name="group")
//	@JsonProperty("groupDefinitions")
//	private ArrayList<GroupDefinition> groupDefinitions;
//	
//	@XmlElement(name="details-header-band")
//	@JsonProperty("detailsHeaderBand")
//	private RootBandFormat detailsHeaderBand;
//	
//	@XmlElement(name="details-footer-band")
//	@JsonProperty("detailsFooterBand")
//	private RootBandFormat detailsFooterBand;
//	
//	@XmlElementWrapper(name="columns")
//	@XmlElement(name="field")
//	@JsonProperty("columns")
//	private ArrayList<FieldDefinition> columns;
//
//	public void setColumns(ArrayList<FieldDefinition> columns) {
//		this.columns = columns;
//	}
//
//	public ArrayList<FieldDefinition> getColumns() {
//		return columns;
//	}
//	
//	public void setDetailsHeaderBand(RootBandFormat detailsHeaderBand) {
//		this.detailsHeaderBand = detailsHeaderBand;
//	}
//
//	public RootBandFormat getDetailsHeaderBand() {
//		return detailsHeaderBand;
//	}
//
//	public void setDetailsFooterBand(RootBandFormat detailsFooterBand) {
//		this.detailsFooterBand = detailsFooterBand;
//	}
//
//	public RootBandFormat getDetailsFooterBand() {
//		return detailsFooterBand;
//	}
//
//	public void setGroupDefinitions(ArrayList<GroupDefinition> groupDefinitions) {
//		this.groupDefinitions = groupDefinitions;
//	}
//
//	public ArrayList<GroupDefinition> getGroupDefinitions() {
//		return groupDefinitions;
//	}
//
//}
