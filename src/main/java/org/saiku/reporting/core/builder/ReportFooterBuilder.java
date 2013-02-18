package org.saiku.reporting.core.builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportFooter;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.filter.types.MessageType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.Label;
import org.saiku.reporting.core.model.ReportSpecification;

public class ReportFooterBuilder extends AbstractBuilder {

	private static final String RPT_FOOTER_MSG =  "rpt-rft-";

	public ReportFooterBuilder(DefaultDataAttributeContext attributeContext,
			AbstractReportDefinition definition,
			DefaultFlowController flowController,
			ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	@Override
	protected void processElement(ReportElement e, int i) {
	
		ArrayList<Label> labels = this.reportSpecification.getReportFooters();
		String uid = RPT_FOOTER_MSG + realIndex;
		
		processElementInternal(e, labels, uid);

	}

	@Override
	public void build() throws ReportProcessingException {

		final ReportFooter reportFooter = definition.getReportFooter();
		if (reportFooter == null) return;
		
		/*
		 * The report footer consists of two parts that need to be processed differently
		 * - The summary band
		 * - The message labels outside of the summary band
		 */
		final Band itemBand = AutoGeneratorUtility.findGeneratedContent(reportFooter);
		itemBand.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "row");
		
		ArrayList<FieldDefinition> fieldDefinitions = this.reportSpecification.getFieldDefinitions();

		final float[] computedWidth = ReportBuilderUtil.correctFieldWidths(fieldDefinitions, definition);
		
		int i =0;
		for (FieldDefinition field : fieldDefinitions) {
			setupReportTotalsField(itemBand, field, computedWidth[i], i);
			i++;
		}

		super.iterateSection(reportFooter);	
		
	}
	
	protected void setupReportTotalsField(final Band totalsBand, final FieldDefinition field, final float width,
			final int fieldIdx) throws ReportProcessingException {
		
		String fieldId = field.getId();
		
		if (StringUtils.isEmpty(fieldId)) {
			return;
		}

		final Class aggFunctionClass = field.getAggregationFunction().getClassObject();

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

		ReportBuilderUtil.setupDefaultGrid(totalsBand, footerElement);

		footerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
		if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.ALLOW_METADATA_STYLING))) {
			footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", field);
		}
		if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
			footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
		}

		String uid = RPT_DETAILS + fieldIdx + LayoutConstants.INNERMOST + LayoutConstants.OUTERMOST;
		String htmlClass = "saiku " + uid;
		
		ElementFormat format = null;
		
		HashMap<String, ElementFormat> m = field.getElementFormats().get(fieldId);
		if(m==null){
			format	= new ElementFormat();
			m = new HashMap<String, ElementFormat>();
			m.put(LayoutConstants.OUTERMOST, format);
			field.getElementFormats().put(fieldId,m);
		}else{
			format = m.get(LayoutConstants.OUTERMOST);
			if(format==null){
				format	= new ElementFormat();
				m.put(LayoutConstants.OUTERMOST, format);
			}
		}

		MergeFormatUtil.mergeElementFormats(footerElement, format);

		footerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass);
		footerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid);
		
		
		totalsBand.addElement(footerElement);

	}

}
