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

public class ReportHeaderBuilder extends AbstractBuilder {

	private String RPT_HEADER_MSG = "rpt-rhd-0-";
	
	@Override
	protected void processElement(ReportElement e, int i) {
	
		ArrayList<Label> titles = this.reportSpecification.getReportHeaders();
		
		ElementFormat format = null;
		String value = null;
				
		if(realIndex < titles.size()){
			Label label = titles.get(realIndex);
			format = label.getFormat();
			value = label.getValue();
		}else{
			//the label is not yet in the the list
			value = (String) e.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE);
			Label label = new Label(value);
			format = new ElementFormat();
			label.setFormat(format);
			titles.add(label);
		}
		
		String uid = RPT_HEADER_MSG + realIndex;

		Element el = (Element) e;
		
		markupElement(e, format, value, uid, el);

//		final Object o = element.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.ELEMENT_TYPE);
//	    if (o instanceof ContentType)
//	    {
//		    System.out.println(
//	    	(String) element.getAttribute("http://reporting.pentaho.org/namespaces/engine/attributes/core", "content-base")
//	    	);
//	    	
//	    	element.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, 
//	    		"http://code-inside.de/blog/wp-content/uploads/image570.png");
//	    }

	}

	@Override
	public void build() throws ReportProcessingException {

		final Section reportHeader = definition.getReportHeader();
		if (reportHeader == null) return;

		super.iterateSection(reportHeader);	
		
	}

	public ReportHeaderBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}
	
	

}
