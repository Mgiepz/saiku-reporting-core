/*******************************************************************************
 * Copyright 2013 Marius Giepz and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

public class ReportHeaderBuilder extends AbstractBuilder {

	private String RPT_HEADER_MSG = "rpt-rhd-";
	
	@Override
	protected void processElement(ReportElement e, int i) {
	
		ArrayList<Label> labels = this.reportSpecification.getReportHeaders();

		String uid = RPT_HEADER_MSG + realIndex;
		
		processElementInternal(e, labels, uid);

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
