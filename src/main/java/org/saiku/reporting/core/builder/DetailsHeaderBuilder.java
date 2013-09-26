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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.ReportSpecification;

public class DetailsHeaderBuilder extends AbstractBuilder{

	public DetailsHeaderBuilder(DefaultDataAttributeContext attributeContext,
			AbstractReportDefinition definition,
			DefaultFlowController flowController,
			ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	private static final Log logger = LogFactory.getLog(DetailsFieldBuilder.class);

	public void build(Band detailsHeader, FieldDefinition field, float width, int j) {
		
		if (StringUtils.isEmpty(field.getId())) {
			return;
		}

		//The Details Header
		generateDetailsHeader(detailsHeader, field, width, j);

	}


	private void generateDetailsHeader(Band detailsHeader,
			FieldDefinition field, float width, int j) {
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

			String hdUid = RPT_DETAILS_HEADER + j;

			String hdHtmlClass = "saiku col-header " + hdUid;

			headerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, hdHtmlClass);
			headerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, hdUid);

			ElementFormat headerFormat = field.getHeaderFormat();

			if(headerFormat==null){
				headerFormat = new ElementFormat();
				field.setHeaderFormat(headerFormat);
			}

			MergeFormatUtil.mergeElementFormats(headerElement, headerFormat);
			
			detailsHeader.addElement(headerElement);
		}
	}


	@Override
	protected void processElement(ReportElement element, int i) {

	}

}
