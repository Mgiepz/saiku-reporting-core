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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.CrosstabCell;
import org.pentaho.reporting.engine.classic.core.CrosstabCellBody;
import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabColumnGroupBody;
import org.pentaho.reporting.engine.classic.core.CrosstabGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabHeader;
import org.pentaho.reporting.engine.classic.core.CrosstabRowGroup;
import org.pentaho.reporting.engine.classic.core.CrosstabRowGroupBody;
import org.pentaho.reporting.engine.classic.core.CrosstabTitleHeader;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.SubGroupBody;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.filter.types.TextFieldType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.types.GroupType;

public class CrosstabBuilder extends AbstractBuilder {

	public CrosstabBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	private static final Log logger = LogFactory.getLog(CrosstabBuilder.class);

	/**
	 * Creating a crosstab report deletes all sub-definitions and the itemband.
	 *
	 * @throws ReportProcessingException if an error occurs.
	 */
	@Override
	public void build() throws ReportProcessingException {

		CrosstabGroup crosstabRootGroup = lookupCrosstab();
		if (crosstabRootGroup == null)
		{
			crosstabRootGroup = new CrosstabGroup();
			insertCrosstab(crosstabRootGroup);
		}

		final CrosstabGroup templateRow = null; //findInnermostRowGroup(definition);
		final CrosstabGroup templateColumn = null; //findInnermostColumnGroup(definition);

		ArrayList<GroupDefinition> groupDefinitions = reportSpecification.getGroupDefinitions();

		Group currentGroup = crosstabRootGroup;

		//
		//	Row Groups		
		//		

		int rowGroupCnt = 0;

		for (GroupDefinition groupDefinition : groupDefinitions) {
			// create a new group and insert it at the end
			if(groupDefinition.getType().equals(GroupType.CT_ROW)){
				if (currentGroup == null)
				{
					final CrosstabRowGroup ctRowGroup;
					if(templateRow!=null){
						ctRowGroup = (CrosstabRowGroup) templateRow.derive();
					}else{
						ctRowGroup = new CrosstabRowGroup();
					}
					if (groupDefinition.getGroupName() != null)
					{
						ctRowGroup.setName(groupDefinition.getGroupName());
					}
					configureCrosstabRowGroup(ctRowGroup, groupDefinition, rowGroupCnt);
//					insertCrosstabRowGroup(ctRowGroup);

				}
				// or reuse an existing group
				else{
					configureCrosstabRowGroup(currentGroup, groupDefinition, rowGroupCnt);

					final GroupBody maybeBody = currentGroup.getBody();

					if (maybeBody instanceof CrosstabRowGroupBody)
					{
						CrosstabRowGroupBody body = (CrosstabRowGroupBody) maybeBody;
						if(body.getGroup() instanceof CrosstabRowGroup)
							currentGroup = (CrosstabRowGroup) body.getGroup();
					}else{
						currentGroup = null;
					}
					currentGroup = null;
				}
				rowGroupCnt++;
			}

			// removing also means that column groups are reattached
			removedUnusedTemplateRowGroups(rowGroupCnt, crosstabRootGroup);

			//
			//		Column Groups		
			//		    

			currentGroup = findOutermostColumnGroupBody(crosstabRootGroup).getGroup();

			int colGroupCnt = 0;

			for (GroupDefinition groupDefinition1 : groupDefinitions) {
				// create a new group and insert it at the end
				if(groupDefinition1.getType().equals(GroupType.CT_COLUMN)){
					if (currentGroup == null)
					{
						final CrosstabColumnGroup ctColGroup;
						if(templateColumn!=null){
							ctColGroup = (CrosstabColumnGroup) templateRow.derive();
						}else{
							ctColGroup = new CrosstabColumnGroup();
						}
						if (groupDefinition1.getGroupName() != null)
						{
							ctColGroup.setName(groupDefinition1.getGroupName());
						}
						configureCrosstabColumnGroup(ctColGroup, groupDefinition1, colGroupCnt);
//						insertCrosstabColGroup(ctColGroup);

					}
					// or reuse an existing group
					else{
						configureCrosstabColumnGroup(currentGroup, groupDefinition1, colGroupCnt);

						final GroupBody maybeBody = currentGroup.getBody();

						if (maybeBody instanceof CrosstabColumnGroupBody)
						{
							CrosstabColumnGroupBody body = (CrosstabColumnGroupBody) maybeBody;
							if(body.getGroup() instanceof CrosstabColumnGroup)
								currentGroup = (CrosstabColumnGroup) body.getGroup();
						}else{
							currentGroup = null;
						}
						currentGroup = null;
					}
					colGroupCnt++;
				}

				// removing also means that the cell body is reattached
				removedUnusedTemplateColumnGroups(colGroupCnt, crosstabRootGroup);	    

				//
				//		Measures		
				//		

				final CrosstabCellBody body = (CrosstabCellBody) currentGroup.getBody();
				CrosstabCell element = new CrosstabCell();
				Element measure = createFieldItem("BC_ORDERDETAILS_PRICEEACH");
				element.addElement(measure);
				body.clear();
				body.addElement(element);
			}

		}

	}

	private void removedUnusedTemplateRowGroups(int rowGroupCnt, CrosstabGroup crosstabRootGroup) {

		CrosstabColumnGroupBody groupBody = findOutermostColumnGroupBody(crosstabRootGroup);

		GroupBody gb = crosstabRootGroup.getBody();
		CrosstabRowGroup innermostGroup = null;
		int i = 0;
		while (gb instanceof CrosstabRowGroupBody && i < rowGroupCnt)
		{
			final CrosstabRowGroupBody rgb = (CrosstabRowGroupBody) gb;
			innermostGroup = rgb.getGroup();
			gb = innermostGroup.getBody();
			i++;
		}

		innermostGroup.setBody(groupBody);
  
	}
	
	private void removedUnusedTemplateColumnGroups(int colGroupCnt, CrosstabGroup crosstabRootGroup) {
		
		CrosstabCellBody cellBody = findCrosstabCellBody(crosstabRootGroup);
		
		GroupBody gb = findOutermostColumnGroupBody(crosstabRootGroup);

		CrosstabColumnGroup innermostGroup = null;
		int i = 0;
		while (gb instanceof CrosstabColumnGroupBody && i < colGroupCnt)
		{
			final CrosstabColumnGroupBody rgb = (CrosstabColumnGroupBody) gb;
			innermostGroup = rgb.getGroup();
			gb = innermostGroup.getBody();
			i++;
		}

		innermostGroup.setBody(cellBody);

	}

	private void configureCrosstabRowGroup(Group currentGroup,
			GroupDefinition groupDefinition, int rowGroupCnt) {

		CrosstabRowGroup group = (CrosstabRowGroup) currentGroup;
		group.setField(groupDefinition.getFieldId());
		group.getTitleHeader().addElement(createDataItem(groupDefinition.getDisplayName()));
		group.getHeader().addElement(createFieldItem(groupDefinition.getFieldId()));

	}
	
	private void configureCrosstabColumnGroup(Group currentGroup,
			GroupDefinition groupDefinition, int colGroupCnt) {

		CrosstabColumnGroup group = (CrosstabColumnGroup) currentGroup;
		group.setField(groupDefinition.getFieldId());
		CrosstabTitleHeader titleHeader = group.getTitleHeader();
		titleHeader.clear();
		titleHeader.addElement(createDataItem(groupDefinition.getDisplayName()));
		CrosstabHeader header = group.getHeader();
		header.clear();
		header.addElement(createFieldItem(groupDefinition.getFieldId()));

	}

	
	private  CrosstabColumnGroupBody findOutermostColumnGroupBody(Group root){

	    while (root instanceof Group)
	    {
	    	if(root instanceof CrosstabColumnGroup){
	    		return (CrosstabColumnGroupBody) ((CrosstabColumnGroup) root).getBody();
	    	}else{
	    		final CrosstabRowGroupBody rgb =  (CrosstabRowGroupBody) root.getBody();
	    		root = rgb.getGroup(); 
	    	}
	    	
	    }

	    return null;

	}
	
	private CrosstabCellBody findCrosstabCellBody(
			CrosstabGroup crosstabRootGroup) {
		return null;
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

	public static Element createDataItem(final String text)
	{
		final Element label = new Element();
		label.setElementType(LabelType.INSTANCE);
		label.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, text);
		label.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, 100f);
		label.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, 50f);
		return label;
	}

	public static Element createFieldItem(final String text)
	{
		final Element label = new Element();
		label.setElementType(TextFieldType.INSTANCE);
		label.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD, text);
		label.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, 100f);
		label.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, 50f);
		return label;
	}

	@Override
	protected void processElement(ReportElement element, int i) {
		// TODO Auto-generated method stub

	}
	
}
