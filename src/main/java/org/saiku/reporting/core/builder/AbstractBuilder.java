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
/**
 * 
 */
package org.saiku.reporting.core.builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.Section;
import org.pentaho.reporting.engine.classic.core.SubReport;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.Label;
import org.saiku.reporting.core.model.ReportSpecification;

/**
 * @author mg
 *
 */
public abstract class AbstractBuilder implements LayoutConstants{

	public AbstractBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super();
		this.attributeContext = attributeContext;
		this.definition = definition;
		this.flowController = flowController;
		this.reportSpecification = reportSpecification;
		
	}

	protected DefaultDataAttributeContext attributeContext;
	protected AbstractReportDefinition definition;
	protected DefaultFlowController flowController;
	protected ReportSpecification reportSpecification;
	
	//es dürfen nur die wirklich relevanten gezählt werden
	protected int realIndex = 0;

	public void build() throws ReportProcessingException{
	}

	/**
	 * We iterate through all Elements of a section. A section is a group-band
	 * or a header...
	 * 
	 * @param s
	 * @param task
	 */
	protected void iterateSection(final Section s) {
		final int count = s.getElementCount();
		for (int i = 0; i < count; i++) {
			final ReportElement element = s.getElement(i);
			processElement(element, i);
			if (element instanceof SubReport) {
				continue;
			}
			if (element instanceof Section) {
				iterateSection((Section) element);
			}
		}
	}

	protected abstract void processElement(ReportElement element, int i);

	protected void markupElement(ReportElement e, ElementFormat format,
			String value, String uid, Element el) {

		if(el.getElementTypeName().equals("message") || el.getElementTypeName().equals("label")||
				e.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_TYPE)!=null){		
			final String htmlClass = "saiku " + uid;
			e.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass);
			e.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid);
			
			e.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING, Boolean.FALSE);

			if(e.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.AGGREGATION_TYPE)==null){

				if(value!=null){
					e.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, value);
				}

			}
			
			MergeFormatUtil.mergeElementFormats(e, format);
			
			realIndex++;
		}
		
		
	}

	protected void processElementInternal(ReportElement e, ArrayList<Label> labels, String uid) {
				ElementFormat format = null;
				String value = null;

				if(realIndex < labels.size() && labels.get(realIndex)!=null){
					Label label = labels.get(realIndex);
					format = label.getFormat();
					if(format == null){
						format = new ElementFormat();
						label.setFormat(format);
					}
					value = label.getValue();
				}
				else if(realIndex < labels.size() && labels.get(realIndex)==null){
					value = (String) e.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE);
					Label label = new Label(value);
					format = new ElementFormat();
					label.setFormat(format);
					labels.set(realIndex, label);
				}else{
					value = (String) e.getAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE);
					Label label = new Label(value);
					format = new ElementFormat();
					label.setFormat(format);
					labels.add(label);
				}
				
				Element el = (Element) e;
				
				markupElement(e, format, value, uid, el);
			}

	protected ElementFormat upsertFormatDefinition(String colGroup, String rowGroup,
			FieldDefinition fieldDefinition) {
				ElementFormat format;
				HashMap<String, ElementFormat> m = fieldDefinition.getElementFormats().get(colGroup);
				if(m==null){
					format	= new ElementFormat();
					m = new HashMap<String, ElementFormat>();
					m.put(rowGroup, format);
					fieldDefinition.getElementFormats().put(colGroup,m);
				}else{
					format = m.get(rowGroup);
					if(format==null){
						format	= new ElementFormat();
						m.put(rowGroup, format);
					}
				}
				return format;
			}


}
