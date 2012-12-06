package org.saiku.reporting.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.GroupFooter;
import org.pentaho.reporting.engine.classic.core.GroupHeader;
import org.pentaho.reporting.engine.classic.core.RelationalGroup;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.SubGroupBody;
import org.pentaho.reporting.engine.classic.core.filter.types.MessageType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.saiku.reporting.core.SaikuReportPreProcessorUtil;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.RootBandFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.types.GroupType;

public class RelationalGroupBuilder extends AbstractBuilder{

	public RelationalGroupBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	public void build() throws ReportProcessingException {

		final Group rootgroup = definition.getRootGroup();
		RelationalGroup group;
		if (!(rootgroup instanceof RelationalGroup)) {
			group = null;
		} else {
			group = (RelationalGroup) rootgroup;
		}

		final RelationalGroup template = findInnermostRelationalGroup(definition);

		final ArrayList<GroupDefinition> groupDefinitions = reportSpecification.getGroupDefinitions();

		if(groupDefinitions ==null) return;
		
		int i = 0;
		for (GroupDefinition groupDefinition : groupDefinitions) {
			if (!GroupType.RELATIONAL.equals(groupDefinition.getType())) {
				continue;
			}

			if (group == null) {
				// create a new group and insert it at the end
				final RelationalGroup relationalGroup;
				if (template != null) {
					relationalGroup = (RelationalGroup) template.derive();
				} else {
					relationalGroup = new RelationalGroup();
				}

				if (groupDefinition.getGroupName() != null) {
					relationalGroup.setName(groupDefinition.getGroupName());
				}
				configureRelationalGroup(relationalGroup, groupDefinition, i);
				insertGroup(relationalGroup);
			} else {
				// modify the existing group
				configureRelationalGroup(group, groupDefinition, i);

				final GroupBody body = group.getBody();
				if (body instanceof SubGroupBody) {
					final SubGroupBody sgBody = (SubGroupBody) body;
					if (sgBody.getGroup() instanceof RelationalGroup) {
						group = (RelationalGroup) sgBody.getGroup();
					} else {
						group = null;
					}
				} else {
					group = null;
				}
			}
		}
		// Remove any group bands are not being used ie. groups with no fields
		GroupUtils.removedUnusedTemplateGroups(groupDefinitions.size(),definition);

		i++;

	}

	private RelationalGroup findInnermostRelationalGroup(final AbstractReportDefinition definition) {
		RelationalGroup retval = null;
		Group existingGroup = definition.getRootGroup();
		while (existingGroup instanceof RelationalGroup) {
			retval = (RelationalGroup) existingGroup;
			final GroupBody body = existingGroup.getBody();
			if (!(body instanceof SubGroupBody)) {
				return retval;
			}
			final SubGroupBody sgb = (SubGroupBody) body;
			existingGroup = sgb.getGroup();
		}

		return retval;
	}

	protected void configureRelationalGroup(final RelationalGroup group, final GroupDefinition groupDefinitionDefinition,
			final int index) throws ReportProcessingException {
		final String groupField = groupDefinitionDefinition.getFieldId();
		if (groupField != null) {
			group.setFieldsArray(new String[] { groupField });
		}

		configureRelationalGroupFooter(group, groupDefinitionDefinition, index);
		configureRelationalGroupHeader(group, groupDefinitionDefinition, index);

	}

	protected void configureRelationalGroupHeader(final RelationalGroup group, final GroupDefinition groupDefinition,
			final int index) {

		RootBandFormat headerDefinition = null;
		
		List<RootBandFormat> headerFormats = groupDefinition.getHeaderFormats();
		
		if(headerFormats.isEmpty()){
			headerDefinition = new RootBandFormat();
			headerFormats.add(0, headerDefinition);
		}else{
			headerDefinition = headerFormats.get(0);
		}
		 
		if (headerDefinition.isVisible()) {
			final GroupHeader header = group.getHeader();
			final Boolean repeat = headerDefinition.isRepeat();
			if (repeat != null) {
				header.setRepeat(repeat.booleanValue());
			}

			final Band content = ReportBuilderUtil.findGeneratedContent(header);
			if (content == null) {
				return;
			}

			final Band headerElement = new Band();
			headerElement.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, BandStyleKeys.LAYOUT_INLINE);
			headerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(-100));
			headerElement.getStyle().setStyleProperty(ElementStyleKeys.DYNAMIC_HEIGHT, Boolean.TRUE);
			headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING,
					Boolean.TRUE);
			headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.LABEL_FOR,
					groupDefinition.getFieldId());
			headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.CACHED_WIZARD_FORMAT_DATA,
					headerDefinition);
			
			final Element headerMessageElement = new Element();
			headerMessageElement.setElementType(new MessageType());
			headerMessageElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD,
					groupDefinition.getFieldId());
			headerMessageElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.NAME,
					groupDefinition.getDisplayName());

			String defaultValue = groupDefinition.getDisplayName() + ": $(" + groupDefinition.getFieldId() + ")";
			String value = headerDefinition.getLabel()!=null ? headerDefinition.getLabel() : defaultValue;
			
			headerMessageElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE,
					value);

			headerElement.addElement(headerMessageElement);
			
			content.clear();
			content.addElement(headerElement);
		}
	}

	private void insertGroup(final RelationalGroup group) {
		Group lastGroup = null;
		Group insertGroup = definition.getRootGroup();
		while (true) {
			if (!(insertGroup instanceof RelationalGroup)) {
				if (lastGroup == null) {
					definition.setRootGroup(group);
					group.setBody(new SubGroupBody(insertGroup));
					return;
				}

				final GroupBody body = lastGroup.getBody();
				final SubGroupBody sgb = new SubGroupBody(group);
				lastGroup.setBody(sgb);
				group.setBody(body);
				return;
			}

			final GroupBody body = insertGroup.getBody();
			if (!(body instanceof SubGroupBody)) {
				final SubGroupBody sgb = new SubGroupBody(group);
				insertGroup.setBody(sgb);
				group.setBody(body);
				return;
			}

			lastGroup = insertGroup;
			final SubGroupBody sgb = (SubGroupBody) body;
			insertGroup = sgb.getGroup();
		}
	}

//	/**
//	 * Removes the unusedTemplateGroups based on the assumption that if a group
//	 * doesn't have any fields assigned to it that it is empty.
//	 * 
//	 * @param definition
//	 */
//	private void removedUnusedTemplateGroups(final int groupsDefined) {
//		final RelationalGroup[] templateRelationalGroups = getTemplateRelationalGroups(definition);
//		final int templateRelationalGroupCount = templateRelationalGroups.length;
//		for (int i = groupsDefined; i < templateRelationalGroupCount; i++) {
//			final RelationalGroup templateRelationalGroup = templateRelationalGroups[i];
//			definition.removeGroup(templateRelationalGroup);
//		}
//	}
//
//	/**
//	 * @param definition
//	 * @return the relational groups in the templates in a flattened array.
//	 */
//	private RelationalGroup[] getTemplateRelationalGroups(AbstractReportDefinition definition) {
//		final ArrayList<RelationalGroup> relationalGroups = new ArrayList<RelationalGroup>();
//		Group group = definition.getRootGroup();
//		while (group != null && group instanceof RelationalGroup) {
//			relationalGroups.add((RelationalGroup) group);
//			final GroupBody body = group.getBody();
//			if (body instanceof SubGroupBody) {
//				final SubGroupBody sgBody = (SubGroupBody) body;
//				if (sgBody.getGroup() instanceof RelationalGroup) {
//					group = sgBody.getGroup();
//				} else {
//					group = null;
//				}
//			} else {
//				group = null;
//			}
//		}
//
//		return relationalGroups.toArray(new RelationalGroup[relationalGroups.size()]);
//	}
//	
	protected void configureRelationalGroupFooter(final RelationalGroup group, final GroupDefinition groupDefinitionDefinition, final int index)
	throws ReportProcessingException {

		final RootBandFormat footerDefinition = groupDefinitionDefinition.getFooterFormat();
		if (footerDefinition.isVisible())
		{
			return;
		}
		
		if(SaikuReportPreProcessorUtil.isCrosstab(reportSpecification)){	
			//funktioniert wie der original wizard
			setupCrosstabOuterGroupFooter(group, groupDefinitionDefinition, index);
		}
		else{
			//funktioniert genauso wie das alte Saiku adhoc
			setupRelationalGroupFooter(group, groupDefinitionDefinition, index);

		}
	}
	
	protected void setupRelationalGroupFooter(final RelationalGroup group, final GroupDefinition groupDefinitionDefinition, final int index)
	throws ReportProcessingException {

		final RootBandFormat footerDefinition = groupDefinitionDefinition.getFooterFormat();
		ArrayList<FieldDefinition> detailFields =  reportSpecification.getFieldDefinitions();

		if (!footerDefinition.isVisible()) {
			return;
		}

		final GroupFooter footer = group.getFooter();
		final Boolean repeat = footerDefinition.isRepeat();
		if (repeat != null) {
			footer.setRepeat(repeat.booleanValue());
		}

		final Band itemBand = AutoGeneratorUtility.findGeneratedContent(footer);
		itemBand.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "row");

		final float[] computedWidth = ReportBuilderUtil.correctFieldWidths(detailFields, definition);
		
		int i =0;
		for (FieldDefinition field : detailFields) {
			setupGroupsummaryField(itemBand, field, computedWidth[i], i);
			i++;
		}

	}
	
	
	
	protected void setupCrosstabOuterGroupFooter(final RelationalGroup group, final GroupDefinition groupDefinitionDefinition, final int index)
	throws ReportProcessingException
	{
//		final RootBandFormat footerDefinition = groupDefinitionDefinition.getFooterFormat();
//		if (footerDefinition.isVisible() == false)
//		{
//			return;
//		}
//
//		if (groupDefinitionDefinition.getAggregationFunction() == null && (groupDefinitionDefinition.getGroupTotalsLabel() == null || groupDefinitionDefinition.getGroupTotalsLabel().length() == 0))
//		{
//			return;
//		}
//
//		final GroupFooter footer = group.getFooter();
//		final Boolean repeat = footerDefinition.getRepeat();
//		if (repeat != null)
//		{
//			footer.setRepeat(repeat.booleanValue());
//		}
//
//		final Band content = AutoGeneratorUtility.findGeneratedContent(footer);
//		if (content == null)
//		{
//			return;
//		}
//
//		final Class aggFunctionClass = groupDefinitionDefinition.getAggregationFunction();
//		final Element footerValueElement = AutoGeneratorUtility.generateFooterElement
//		(aggFunctionClass, ReportBuilderUtil.computeElementType(groupDefinitionDefinition, flowController),
//				groupDefinitionDefinition.getGroupName(), groupDefinitionDefinition.getFieldId());
//
//		final Element footerLabelElement = new Element();
//		footerLabelElement.setElementType(new LabelType());
//		if (groupDefinitionDefinition.getGroupTotalsLabel() != null)
//		{
//			footerLabelElement.setAttribute
//			(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, groupDefinitionDefinition.getGroupTotalsLabel());
//		}
//		else
//		{
//			footerLabelElement.setAttribute
//			(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, groupDefinitionDefinition.getFieldId());
//			footerLabelElement.setAttribute
//			(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.LABEL_FOR, groupDefinitionDefinition.getFieldId());
//			footerLabelElement.setAttribute(AttributeNames.Wizard.NAMESPACE,
//					AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES, Boolean.TRUE);
//		}
//
//		final Band footerElement = new Band();
//		footerElement.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, BandStyleKeys.LAYOUT_INLINE);
//		footerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(-100));
//		footerElement.getStyle().setStyleProperty(ElementStyleKeys.DYNAMIC_HEIGHT, Boolean.TRUE);
//		footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING,
//				Boolean.TRUE);
//		footerElement.setAttribute
//		(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.LABEL_FOR, groupDefinitionDefinition.getFieldId());
//		footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.CACHED_WIZARD_FORMAT_DATA, footerDefinition);
//		footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.CACHED_WIZARD_FIELD_DATA, groupDefinitionDefinition);
//		footerElement.addElement(footerLabelElement);
//		footerElement.addElement(footerValueElement);
//
//		content.clear();
//		content.addElement(footerElement);
		}
	
	protected void setupGroupsummaryField(final Band groupSummaryBand, final FieldDefinition field, final float width,
			final int fieldIdx) throws ReportProcessingException {
		if (StringUtils.isEmpty(field.getId())) {
			return;
		}

		final Class aggFunctionClass = field.getAggregationFunction().getClassObject();

		// If an aggregation is set we assume that the user wants the summary to
		// be shown
		Element footerElement = null;

		if (aggFunctionClass != null) {
			footerElement = ReportBuilderUtil.generateFooterElement(aggFunctionClass, 
					ReportBuilderUtil.computeElementType(field.getId(), flowController),
					null,
					field.getId()
					);
		}
		// otherwise we show a messagelabel where the user can enter additional
		// info
		else {
			footerElement = new Element();
			footerElement.setElementType(new MessageType());
		}

		ReportBuilderUtil.setupDefaultGrid(groupSummaryBand, footerElement);

		footerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
		if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.ALLOW_METADATA_STYLING))) {
			footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", field);
		}
		if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
			footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
		}

		groupSummaryBand.addElement(footerElement);

	}

	@Override
	protected void processElement(ReportElement element, int i) {
		// DO NOTHING
	}

}
