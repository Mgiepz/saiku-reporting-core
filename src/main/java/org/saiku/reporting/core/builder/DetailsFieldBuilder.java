/**
 * 
 */
package org.saiku.reporting.core.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.metadata.ElementType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.ReportSpecification;

/**
 * @author mg
 *
 */
public class DetailsFieldBuilder extends AbstractBuilder{
	
	public DetailsFieldBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	private static final Log logger = LogFactory.getLog(DetailsFieldBuilder.class);

	public static Element build(String fieldId, ElementType computeElementType) {
		   final Element detailsElement = new Element();
		    detailsElement.getStyle().setStyleProperty(ElementStyleKeys.DYNAMIC_HEIGHT, Boolean.TRUE);
		    detailsElement.setElementType(computeElementType);
		    detailsElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FIELD, fieldId);
		    detailsElement.setAttribute(AttributeNames.Wizard.NAMESPACE,
		        AttributeNames.Wizard.ALLOW_METADATA_STYLING, Boolean.TRUE);
		    detailsElement.setAttribute(AttributeNames.Wizard.NAMESPACE,
		        AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES, Boolean.TRUE);
		    return detailsElement;
	}

	public void build(Band detailsHeader, Band detailsFooter, Band itemBand, FieldDefinition field, float width, int j) {
			if (StringUtils.isEmpty(field.getId())) {
				return;
			}

			final Element detailElement = AutoGeneratorUtility.generateDetailsElement(field.getId(),
					ReportBuilderUtil.computeElementType(field.getId(), flowController));
			ReportBuilderUtil.setupDefaultGrid(itemBand, detailElement);

			final String id = "wizard::details-" + field.getId();
			detailElement.setName(id);
			detailElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
			
			if (Boolean.TRUE.equals(detailElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
					AttributeNames.Wizard.ALLOW_METADATA_STYLING))) {
				detailElement.setAttribute("http://reporting.pentaho.org/namespaces/engine/attributes/wizard",
						"CachedWizardFormatData", field);
				detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", field);
			}
			if (Boolean.TRUE.equals(detailElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
					AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
				detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
			}
			itemBand.addElement(detailElement);

			if (Boolean.TRUE.equals(field.isHideRepeating())) {
				detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ONLY_SHOW_CHANGING_VALUES,
						Boolean.TRUE);
			}

			if (detailsHeader != null) {
				final Element headerElement = AutoGeneratorUtility.generateHeaderElement(field.getId());
				ReportBuilderUtil.setupDefaultGrid(detailsHeader, headerElement);
				headerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));

				if (Boolean.TRUE.equals(headerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
						AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
					headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
				}

				headerElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE,
						field.getName());

				headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING,
						false);
				headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES,
						false);

				detailsHeader.addElement(headerElement);
			}

			if (detailsFooter != null) {
				final Class aggFunctionClass = field.getAggregationFunction().getClassObject();

				final Element footerElement = AutoGeneratorUtility.generateFooterElement(aggFunctionClass,
						ReportBuilderUtil.computeElementType(field.getId(), flowController), null, field.getId());

				ReportBuilderUtil.setupDefaultGrid(detailsFooter, footerElement);

				footerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
				if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
						AttributeNames.Wizard.ALLOW_METADATA_STYLING))) {
					footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", field);
				}
				if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
						AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
					footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
				}

				//this will be hidden most of the times
				detailsFooter.addElement(footerElement);
			}
		}

	@Override
	protected void processElement(ReportElement element, int i) {
		// TODO Auto-generated method stub
		
	}

}
