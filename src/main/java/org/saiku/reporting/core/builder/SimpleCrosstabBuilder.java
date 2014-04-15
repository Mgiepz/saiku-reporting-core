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
package org.saiku.reporting.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.CrosstabCell;
import org.pentaho.reporting.engine.classic.core.CrosstabCellBody;
import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroupBody;
import org.pentaho.reporting.engine.classic.core.CrosstabGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabRowGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabRowGroupBody;
import org.pentaho.reporting.engine.classic.core.DetailsHeader;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.SubGroupBody;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.filter.types.NumberFieldType;
import org.pentaho.reporting.engine.classic.core.function.TotalGroupSumFunction;
import org.pentaho.reporting.engine.classic.core.metadata.ElementType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributes;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.RootBandFormat;
import org.saiku.reporting.core.model.types.GroupType;

public class SimpleCrosstabBuilder extends AbstractBuilder {

	

	public SimpleCrosstabBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	private static final Log logger = LogFactory.getLog(SimpleCrosstabBuilder.class);

	private static Element createLabel(final String text)
	{
		final Element label = new Element();
		label.setElementType(LabelType.INSTANCE);
		label.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, text);
		label.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, 80f);
		label.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, 20f);
		return label;
	}

	private Element createFieldItem(final String text)
	{
		return createFieldItem(text, null, null);
	}

	private Element createFieldItem(final String fieldName,
			final String aggregationGroup,
			final Class aggregationType)
	{

		final DataAttributes attributes = this.flowController.getDataSchema().getAttributes(fieldName);
		final ElementType targetType = AutoGeneratorUtility.createFieldType(attributes, this.attributeContext);

		final Element element = new Element();
		element.setElementType(targetType);
		element.getElementType().configureDesignTimeDefaults(element, Locale.getDefault());

		if (targetType instanceof NumberFieldType)
		{
			element.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FORMAT_STRING, "0.00;-0.00");
		}

		element.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD, fieldName);
		element.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, -100f); //was 80
		element.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
		
		element.getStyle().setStyleProperty(ElementStyleKeys.ALIGNMENT,ElementAlignment.LEFT);
		
		element.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_TYPE, aggregationType);
		element.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_GROUP, aggregationGroup);
		//element.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING, Boolean.TRUE);
		return element;
	}

	private CrosstabCell createCell(final String aggregationGroup, String colGroup, String rowGroup)
	{
		final CrosstabCell cell = new CrosstabCell();
		
		cell.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
		cell.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, BandStyleKeys.LAYOUT_ROW);
		
		ArrayList<FieldDefinition> fields = this.reportSpecification.getFieldDefinitions();

		final float[] computedWidth = ReportBuilderUtil.correctFieldWidths(fields, definition);
		
		
		int i = 0;
		for (FieldDefinition fieldDefinition : fields) {
			
			//jetzt noch aus den field definition
			String uid = RPT_DETAILS + i + "-" + colGroup + "-" + rowGroup;
			String htmlClass = "saiku " + uid;


			//Element fieldItem = createFieldItem(fieldDefinition.getId(), group, fieldDefinition.getAggregationFunction().getClass());
			Element fieldItem = createFieldItem(fieldDefinition.getId(), aggregationGroup, TotalGroupSumFunction.class);
			
			fieldItem.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(computedWidth[i]));
			
			ElementFormat format = upsertFormatDefinition(colGroup, rowGroup, fieldDefinition);

			MergeFormatUtil.mergeElementFormats(fieldItem, format);
			
			
			fieldItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass);
			fieldItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid);
			
			cell.addElement(fieldItem);
			i++;
		}

		return cell;
	}

	public void build() throws ReportProcessingException {

		/*
		 * Generate the Details-Row
		 */
		final CrosstabCellBody cellBody = new CrosstabCellBody();
		
		//---------------------------------
		DetailsHeader detailsHeader = new DetailsHeader();
		detailsHeader.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
		detailsHeader.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, BandStyleKeys.LAYOUT_ROW);
		ArrayList<FieldDefinition> detailFieldDefinitions = this.reportSpecification.getFieldDefinitions();
		final float[] computedWidth = ReportBuilderUtil.correctFieldWidths(detailFieldDefinitions, definition);
		DetailsHeaderBuilder detailsHeaderBuilder = new DetailsHeaderBuilder(attributeContext, definition, flowController, reportSpecification);
		int j=0;
		for (FieldDefinition field : detailFieldDefinitions) {
			detailsHeaderBuilder.build(detailsHeader, field, computedWidth[j], j);	
			j++;
		}
		cellBody.setHeader(detailsHeader);
		//---------------------------------
		
		
		cellBody.addElement(createCell(null,INNERMOST,INNERMOST));

		GroupBody body = cellBody;

		/*
		 * Generate the Column-Groups
		 */
		ArrayList<GroupDefinition> groupDefinitions = this.reportSpecification.getGroupDefinitions();
		
		ListIterator<GroupDefinition> colGrpItr = groupDefinitions.listIterator(groupDefinitions.size());
		
		while(colGrpItr.hasPrevious()){
	    	final GroupDefinition colGrp = colGrpItr.previous();
			if(colGrp.getType().equals(GroupType.CT_COLUMN)){
				final CrosstabColumnGroup columnGroup = new CrosstabColumnGroup(body);

				columnGroup.setName("Group " +  colGrp.getGroupName());
				columnGroup.setField(colGrp.getFieldId());

				/*
			    1) Chrosstab Header
			    2) Title Header
			    3) Summary Header
				 */
				final List<RootBandFormat> headerFormats = colGrp.getHeaderFormats();

				int index = groupDefinitions.indexOf(colGrp);
				String uid = RPT_GROUP_HEADER + index;
				String htmlClass = "saiku " + uid;

				RootBandFormat headerFormat = null; 
				
				if(headerFormats.size() < 1 || headerFormats.get(0)==null){
					headerFormat=new RootBandFormat();
					headerFormats.add(0, headerFormat);
				}else{
					headerFormat = headerFormats.get(0);
				}
				
				Element headerItem = createFieldItem(colGrp.getFieldId());
				
				//MergeFormatUtil.mergeElementFormats(headerItem, headerFormat);
				MergeFormatUtil.mergeElementFormats(columnGroup.getHeader(), headerFormat);
				
				headerItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass + "-0");
				headerItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid + "-0");
				
				//Is this used for style definitions?				
				headerItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Xml.ID, uid + "-0");
				
				
				columnGroup.getHeader().addElement(headerItem);
				columnGroup.getHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				columnGroup.getHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.TRUE);
				
				RootBandFormat titleFormat = null; 
				
				if(headerFormats.size() < 2 || headerFormats.get(1)==null){
					titleFormat=new RootBandFormat();
					headerFormats.add(1, titleFormat);
				}else{
					titleFormat = headerFormats.get(1);
				}

				String titleLabel = titleFormat.getLabel()!=null ? titleFormat.getLabel() : colGrp.getDisplayName();
				Element titleItem = createLabel(titleLabel);
				
				MergeFormatUtil.mergeElementFormats(titleItem, titleFormat);
							
				titleItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass + "-1");
				titleItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid + "-1");
				columnGroup.getTitleHeader().addElement(titleItem);
				columnGroup.getTitleHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				
				RootBandFormat summaryFormat = null; 
				
				if(headerFormats.size() < 3 || headerFormats.get(2)==null){
					summaryFormat=new RootBandFormat();
					headerFormats.add(1, summaryFormat);
				}else{
					summaryFormat = headerFormats.get(2);
				}

				String summaryLabel = summaryFormat.getLabel()!=null?summaryFormat.getLabel():"Total";
				Element summaryItem = createLabel(summaryLabel);
				
				MergeFormatUtil.mergeElementFormats(summaryItem, summaryFormat);				
				
				summaryItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass + "-2");
				summaryItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid + "-2");				
				columnGroup.getSummaryHeader().addElement(summaryItem);
				columnGroup.getSummaryHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				columnGroup.setPrintSummary(colGrp.isPrintSummary());
				
				if(colGrp.isPrintSummary()){
					//create cell needs to be enhanced later to always pick the correct group combination for the
					//element formats
					
					final CrosstabCell cell = createCell("Group " + colGrp.getFieldId(), colGrp.getFieldId(), INNERMOST);
					cell.setColumnField(colGrp.getFieldId());
					cell.setName(colGrp.getFieldId());
					cellBody.addElement(cell);				
				}
				body = new CrosstabColumnGroupBody(columnGroup);
			}
	    }

		/*
		 * Generate the Row-Groups
		 */
		ListIterator<GroupDefinition> rowGrpItr =  groupDefinitions.listIterator(groupDefinitions.size());
	    
	    while(rowGrpItr.hasPrevious()){
	    	final GroupDefinition rowGrp = rowGrpItr.previous();
			if(rowGrp.getType().equals(GroupType.CT_ROW)){
				final CrosstabRowGroup rowGroup = new CrosstabRowGroup(body);
				int index = groupDefinitions.indexOf(rowGrp);
				String uid = RPT_GROUP_HEADER + index;
				String htmlClass = "saiku " + uid;
				
				rowGroup.setName("Group " +  rowGrp.getGroupName());
				rowGroup.setField(rowGrp.getFieldId());
				
				final List<RootBandFormat> headerFormats = rowGrp.getHeaderFormats();

				RootBandFormat headerFormat = null; 
				
				if(headerFormats.size() < 1 || headerFormats.get(0)==null){
					headerFormat=new RootBandFormat();
					headerFormats.add(0, headerFormat);
				}else{
					headerFormat = headerFormats.get(0);
				}
			
				Element headerItem = createFieldItem(rowGrp.getFieldId());
				
				//MergeFormatUtil.mergeElementFormats(headerItem, headerFormat);
				MergeFormatUtil.mergeElementFormats(rowGroup.getHeader(), headerFormat);
				
				headerItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass + "-0");
				headerItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid + "-0");
				
				rowGroup.getHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				rowGroup.getHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.TRUE);
				rowGroup.getHeader().addElement(headerItem);
				
				RootBandFormat titleFormat = null; 
				
				if(headerFormats.size() < 2 || headerFormats.get(1)==null){
					titleFormat=new RootBandFormat();
					headerFormats.add(1, titleFormat);
				}else{
					titleFormat = headerFormats.get(1);
				}
				
				String titleLabel = titleFormat.getLabel()!=null ? titleFormat.getLabel() : rowGrp.getDisplayName();
				Element titleItem = createLabel(titleLabel);
				
				MergeFormatUtil.mergeElementFormats(titleItem, titleFormat);

				titleItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass + "-1");
				titleItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid + "-1");
				
				rowGroup.getTitleHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				rowGroup.getTitleHeader().addElement(titleItem);
				
				RootBandFormat summaryFormat = null; 
				
				if(headerFormats.size() < 3 || headerFormats.get(2)==null){
					summaryFormat=new RootBandFormat();
					headerFormats.add(1, summaryFormat);
				}else{
					summaryFormat = headerFormats.get(2);
				}
				
				String summaryLabel = summaryFormat.getLabel()!=null?summaryFormat.getLabel():"Total";
				Element summaryItem = createLabel(summaryLabel);
				
				MergeFormatUtil.mergeElementFormats(summaryItem, summaryFormat);

				summaryItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass + "-2");
				summaryItem.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid + "-2");	
				rowGroup.getSummaryHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				rowGroup.getSummaryHeader().addElement(summaryItem);
				rowGroup.setPrintSummary(rowGrp.isPrintSummary());
				
				if(rowGrp.isPrintSummary()){
					final CrosstabCell cell = createCell("Group " + rowGroup.getField(), INNERMOST, rowGrp.getFieldId());
					cell.setRowField(rowGrp.getFieldId());
					cell.setName(rowGrp.getFieldId());
					cellBody.addElement(cell);

					ListIterator<GroupDefinition> colGrpItrInner = groupDefinitions.listIterator(groupDefinitions.size());
					
				    while(colGrpItrInner.hasPrevious()){
				    	final GroupDefinition colGrp = colGrpItrInner.previous();
						if(colGrp.getType().equals(GroupType.CT_COLUMN)){
							if (colGrp.isPrintSummary())
							{
								final CrosstabCell crosstabCell = createCell("Group " + rowGroup.getField(), colGrp.getFieldId(), rowGrp.getFieldId());
								crosstabCell.setColumnField(colGrp.getFieldId());
								crosstabCell.setRowField(rowGrp.getFieldId());
								crosstabCell.setName(colGrp.getFieldId() + "," + rowGroup.getField());
								cellBody.addElement(crosstabCell);
							}
						}
					}			
				}
				body = new CrosstabRowGroupBody(rowGroup);
			}
	    }
		/*
		 * Let's ommit Other-Groups for now
		 */


		// At The end we need to wrap the generated stuff in a crosstabgroup and put it in the
		// report
		CrosstabGroup crosstabRootGroup = new CrosstabGroup(body);
		crosstabRootGroup.setPrintColumnTitleHeader(true);
		crosstabRootGroup.setPrintDetailsHeader(true);
		insertCrosstab(crosstabRootGroup);
		
		//remove all relational groups
		GroupUtils.removedUnusedTemplateGroups(0,definition);

	}	
	
	/**
	 * Inserts the crosstab into the report as innermost group. This method will fail if there is already a crosstab
	 * active.
	 *
	 * @param crosstabGroup
	 */
	private void insertCrosstab(final CrosstabGroup crosstabGroup)
	{
		Group existingGroup = definition.getRootGroup();
		GroupBody gb = existingGroup.getBody();
		while (gb instanceof SubGroupBody)
		{
			final SubGroupBody sgb = (SubGroupBody) gb;
			existingGroup = sgb.getGroup();
			gb = existingGroup.getBody();
		}
		existingGroup.setBody(new SubGroupBody(crosstabGroup));
	}

	private CrosstabGroup lookupCrosstab()
	{
		Group existingGroup = definition.getRootGroup();
		if (existingGroup instanceof CrosstabGroup)
		{
			return (CrosstabGroup) existingGroup;
		}

		GroupBody gb = existingGroup.getBody();
		while (gb instanceof SubGroupBody)
		{
			final SubGroupBody sgb = (SubGroupBody) gb;
			existingGroup = sgb.getGroup();
			if (existingGroup instanceof CrosstabGroup)
			{
				return (CrosstabGroup) existingGroup;
			}
			gb = existingGroup.getBody();
		}

		return null;
	}

	@Override
	protected void processElement(ReportElement element, int i) {
		// TODO Auto-generated method stub

	}

}
