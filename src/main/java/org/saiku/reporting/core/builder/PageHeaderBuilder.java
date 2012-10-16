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

public class PageHeaderBuilder extends AbstractBuilder {

	private String PAGE_HEADER_MSG = "rpt-phd-0-";
	
	public PageHeaderBuilder(DefaultDataAttributeContext attributeContext,
			AbstractReportDefinition definition,
			DefaultFlowController flowController,
			ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	@Override
	protected void processElement(ReportElement e, int i) {

		ArrayList<Label> pageHeaders = this.reportSpecification.getPageHeaders();
		
		ElementFormat format = null;
		String value = null;
				
		if(realIndex < pageHeaders.size()){
			Label label = pageHeaders.get(realIndex);
			format = label.getFormat();
			value = label.getValue();
		}else{
			//the label is not yet in the the list
			value = (String) e.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE);
			Label label = new Label(value);
			format = new ElementFormat();
			label.setFormat(format);
			pageHeaders.add(label);
		}
		
		String uid = PAGE_HEADER_MSG + realIndex;
		
		//markup the element
		
		Element el = (Element) e;
		
		markupElement(e, format, value, uid, el);

	}
	
	@Override
	public void build() throws ReportProcessingException {

		final Section pageHeader = definition.getPageHeader();
		if (pageHeader == null) return;

		super.iterateSection(pageHeader);	
		
	}

}
