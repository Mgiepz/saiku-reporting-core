package org.saiku.reporting.core.builder;

import java.util.ArrayList;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.Label;
import org.saiku.reporting.core.model.ReportSpecification;

public class ReportFooterBuilder extends AbstractBuilder {

	private static final String RPT_FOOTER_MSG =  "rpt-rft-0-";

	public ReportFooterBuilder(DefaultDataAttributeContext attributeContext,
			AbstractReportDefinition definition,
			DefaultFlowController flowController,
			ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	@Override
	protected void processElement(ReportElement e, int i) {
	
		ArrayList<Label> footers = this.reportSpecification.getReportFooters();
		
		ElementFormat format = null;
		String value = null;
				
		if(realIndex < footers.size()){
			Label label = footers.get(realIndex);
			format = label.getFormat();
			value = label.getValue();
		}else{
			//the label is not yet in the the list
			value = (String) e.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE);
			Label label = new Label(value);
			format = new ElementFormat();
			label.setFormat(format);
			footers.add(label);
		}
		
		String uid = RPT_FOOTER_MSG + realIndex;

		Element el = (Element) e;
		
		markupElement(e, format, value, uid, el);


	}

	@Override
	public void build() throws ReportProcessingException {

		final Section reportFooter = definition.getReportFooter();
		if (reportFooter == null) return;

		super.iterateSection(reportFooter);	
		
	}

}
