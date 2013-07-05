package org.saiku.reporting.core.builder;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BandStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.ReportSpecification;

public class TableBuilder extends AbstractBuilder{
	
	private static final Log logger = LogFactory.getLog(TableBuilder.class);
	
	public TableBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}
	
	@Override
	public void build() throws ReportProcessingException {

		ArrayList<FieldDefinition> detailFieldDefinitions = reportSpecification.getFieldDefinitions(); 
		
		if (detailFieldDefinitions.isEmpty()) {
			return;
		}

		definition.getDetailsFooter().setVisible(false);
		
		final Band detailsHeader = ReportBuilderUtil.findGeneratedContent(definition.getDetailsHeader());
		final Band detailsFooter = ReportBuilderUtil.findGeneratedContent(definition.getDetailsFooter());
		final Band itemBand = AutoGeneratorUtility.findGeneratedContent(definition.getItemBand());

		if (itemBand == null) {
			return;
		}

		final float[] computedWidth = ReportBuilderUtil.correctFieldWidths(detailFieldDefinitions, definition);
		
		itemBand.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "row");
		
		if (detailsHeader != null) {
			detailsHeader.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "row");
		}
		if (detailsFooter != null) {
			detailsFooter.getStyle().setStyleProperty(BandStyleKeys.LAYOUT, "row");
		}

		DetailsFieldBuilder detailsFieldBuilder = new DetailsFieldBuilder(attributeContext, definition, flowController, reportSpecification);
		DetailsHeaderBuilder detailsHeaderBuilder = new DetailsHeaderBuilder(attributeContext, definition, flowController, reportSpecification);
		int j=0;
		for (FieldDefinition field : detailFieldDefinitions) {
			detailsHeaderBuilder.build(detailsHeader, field, computedWidth[j], j);	
			detailsFieldBuilder.build(detailsHeader, detailsFooter, itemBand, field, computedWidth[j], j);	
			j++;
		}

		if (detailsFooter != null) {
			final Element[] elements = detailsFooter.getElementArray();
			boolean footerEmpty = true;
			for (int i = 0; i < elements.length; i++) {
				final Element element = elements[i];
				if (!"label".equals(element.getElementTypeName())) {
					footerEmpty = false;
					break;
				}
			}
			if (footerEmpty) {
				detailsFooter.clear();
			}
		}

	}

	@Override
	protected void processElement(ReportElement element, int i) {
		// DO NOTHING		
	}

}
