package org.saiku.reporting.core.builder;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.RootBandFormat;
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

			String uid = RPT_GROUP_HEADER + index + "-0"; //relational group has only one header

			String htmlClass = "saiku " + uid;
			
			headerMessageElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass);
			headerMessageElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid);

			MergeFormatUtil.mergeElementFormats(headerMessageElement, headerDefinition);
			
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

	protected void configureRelationalGroupFooter(final RelationalGroup group, final GroupDefinition groupDefinition, final int index)
	throws ReportProcessingException {

		final RootBandFormat footerDefinition = groupDefinition.getFooterFormat();
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
			setupGroupsummaryField(itemBand, field, groupDefinition, computedWidth[i], i);
			i++;
		}

	}
	
	protected void setupGroupsummaryField(final Band groupSummaryBand, final FieldDefinition fieldDefinition, GroupDefinition groupDefinition, final float width,
			final int fieldIdx) throws ReportProcessingException {
		
		String fieldId = groupDefinition.getFieldId();
		
		if (StringUtils.isEmpty(fieldId)) {
			return;
		}

		final Class aggFunctionClass = fieldDefinition.getAggregationFunction().getClassObject();

		// If an aggregation is set we assume that the user wants the summary to
		// be shown
		Element footerElement = null;

		if (aggFunctionClass != null) {
			footerElement = ReportBuilderUtil.generateFooterElement(aggFunctionClass, 
					ReportBuilderUtil.computeElementType(fieldId, flowController),
					null,
					fieldId
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
			footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", fieldDefinition);
		}
		if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
			footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", fieldDefinition);
		}
		
		//jetzt noch aus den field definition
		
		String relGroup = fieldDefinition.getId();
		
		String uid = RPT_DETAILS + fieldIdx + "-" + INNERMOST + "-" + relGroup;
		String htmlClass = "saiku " + uid;
		
		ElementFormat format = null;
		
		HashMap<String, ElementFormat> m = fieldDefinition.getElementFormats().get(fieldId);
		if(m==null){
			format	= new ElementFormat();
			m = new HashMap<String, ElementFormat>();
			m.put(relGroup, format);
			fieldDefinition.getElementFormats().put(fieldId,m);
		}else{
			format = m.get(relGroup);
			if(format==null){
				format	= new ElementFormat();
				m.put(relGroup, format);
			}
		}

		MergeFormatUtil.mergeElementFormats(footerElement, format);

		footerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass);
		footerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid);

		groupSummaryBand.addElement(footerElement);

	}

	@Override
	protected void processElement(ReportElement element, int i) {
		// DO NOTHING
	}

}
