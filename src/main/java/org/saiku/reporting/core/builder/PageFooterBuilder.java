package org.saiku.reporting.core.builder;

import java.util.ArrayList;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.Label;
import org.saiku.reporting.core.model.ReportSpecification;

public class PageFooterBuilder extends AbstractBuilder {

	private static final String PAGE_FOOTER_MSG = "rpt-pft-";

	public PageFooterBuilder(DefaultDataAttributeContext attributeContext,
			AbstractReportDefinition definition,
			DefaultFlowController flowController,
			ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	@Override
	protected void processElement(ReportElement e, int i) {

		ArrayList<Label> labels = this.reportSpecification.getPageFooters();

		String uid = PAGE_FOOTER_MSG + realIndex;;
		
		processElementInternal(e, labels, uid);

	}
	
	@Override
	public void build() throws ReportProcessingException {

		final Section pageHeader = definition.getPageFooter();
		if (pageHeader == null) return;

		super.iterateSection(pageHeader);	
		
	}

}
