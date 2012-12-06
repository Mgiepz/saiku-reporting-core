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
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.SubGroupBody;
import org.pentaho.reporting.engine.classic.core.filter.types.LabelType;
import org.pentaho.reporting.engine.classic.core.filter.types.NumberFieldType;
import org.pentaho.reporting.engine.classic.core.metadata.ElementType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributes;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
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

	private static final String RPT_DETAILS = "rpt-dtl-";
	
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
		element.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, 80f);
		element.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, 20f);
		
		element.getStyle().setStyleProperty(ElementStyleKeys.ALIGNMENT,ElementAlignment.LEFT);
		
		element.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_TYPE, aggregationType);
		element.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_GROUP, aggregationGroup);
		//element.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING, Boolean.TRUE);
		return element;
	}

	private CrosstabCell createCell(final String group)
	{
		final CrosstabCell cell = new CrosstabCell();
		cell.getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
		cell.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, BandStyleKeys.LAYOUT_ROW);
	
		ArrayList<FieldDefinition> fields = this.reportSpecification.getFieldDefinitions();

		int i = 0;
		for (FieldDefinition fieldDefinition : fields) {
			
			String uid = RPT_DETAILS + i;
			String htmlClass = "saiku " + uid;

			Element fieldItem = createFieldItem(fieldDefinition.getId(), group, fieldDefinition.getAggregationFunction().getClass());

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
		cellBody.addElement(createCell(null));

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
		
				columnGroup.getHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				columnGroup.getHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.TRUE);
				columnGroup.getHeader().addElement(createFieldItem(colGrp.getFieldId()));
				columnGroup.getTitleHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				columnGroup.getTitleHeader().addElement(createLabel(colGrp.getDisplayName()));
				columnGroup.getSummaryHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				//columnGroup.getSummaryHeader().addElement(createLabel(colGrp.getFieldId()));
				columnGroup.getSummaryHeader().addElement(createLabel("Total"));
				
				if(colGrp.isPrintSummary()){
					//create cell needs to be enhanced later to always pick the correct group combination for the
					//element formats
					final CrosstabCell cell = createCell("Group " + colGrp.getFieldId());
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
				rowGroup.setName("Group " +  rowGrp.getGroupName());
				rowGroup.setField(rowGrp.getFieldId());
				rowGroup.getHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				rowGroup.getHeader().getStyle().setStyleProperty(TextStyleKeys.BOLD, Boolean.TRUE);
				rowGroup.getHeader().addElement(createFieldItem(rowGrp.getFieldId()));

				rowGroup.getTitleHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				rowGroup.getTitleHeader().addElement(createLabel(rowGrp.getDisplayName()));

				rowGroup.getSummaryHeader().getStyle().setStyleProperty(ElementStyleKeys.MIN_HEIGHT, -100f);
				//rowGroup.getSummaryHeader().addElement(createLabel(rowGrp.getFieldId()));
				rowGroup.getSummaryHeader().addElement(createLabel("Total"));
				
				if(rowGrp.isPrintSummary()){
					final CrosstabCell cell = createCell("Group " + rowGroup.getField());
					cell.setRowField(rowGrp.getFieldId());
					cell.setName(rowGrp.getFieldId());
					cellBody.addElement(cell);

					ListIterator<GroupDefinition> colGrpItrInner = groupDefinitions.listIterator(groupDefinitions.size());
					
				    while(colGrpItrInner.hasPrevious()){
				    	final GroupDefinition colGrp = colGrpItrInner.previous();
						if(colGrp.getType().equals(GroupType.CT_COLUMN)){
							if (colGrp.isPrintSummary())
							{
								final CrosstabCell crosstabCell = createCell("Group " + rowGroup.getField());
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
