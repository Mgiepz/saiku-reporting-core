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

public class PageFooterBuilder extends AbstractBuilder {

	private static final String PAGE_FOOTER_MSG = "rpt-pft-0-";

	public PageFooterBuilder(DefaultDataAttributeContext attributeContext,
			AbstractReportDefinition definition,
			DefaultFlowController flowController,
			ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	@Override
	protected void processElement(ReportElement e, int i) {

		ArrayList<Label> pageFooters = this.reportSpecification.getPageFooters();
		
		ElementFormat format = null;
		String value = null;
				
		if(realIndex < pageFooters.size()){
			Label label = pageFooters.get(realIndex);
			format = label.getFormat();
			value = label.getValue();
		}else{
			//the label is not yet in the the list
			value = (String) e.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE);
			Label label = new Label(value);
			format = new ElementFormat();
			label.setFormat(format);
			pageFooters.add(label);
		}
		
		String uid = PAGE_FOOTER_MSG + realIndex;
		
		//markup the element
		
		Element el = (Element) e;
		
		markupElement(e, format, value, uid, el);

	}
	
	@Override
	public void build() throws ReportProcessingException {

		final Section pageHeader = definition.getPageFooter();
		if (pageHeader == null) return;

		super.iterateSection(pageHeader);	
		
	}

}
