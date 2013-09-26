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

/**
 * @author mg
 *
 */
public class DetailsFieldBuilder extends AbstractBuilder{
	
	public DetailsFieldBuilder(DefaultDataAttributeContext attributeContext, AbstractReportDefinition definition,
			DefaultFlowController flowController, ReportSpecification reportSpecification) {
		super(attributeContext, definition, flowController, reportSpecification);
	}

	private static final Log logger = LogFactory.getLog(DetailsFieldBuilder.class);

	public void build(Band detailsHeader, Band detailsFooter, Band itemBand, FieldDefinition field, float width, int j) {
			if (StringUtils.isEmpty(field.getId())) {
				return;
			}

			//The Details Field
			
			final Element detailElement = AutoGeneratorUtility.generateDetailsElement(field.getId(),
					ReportBuilderUtil.computeElementType(field.getId(), flowController));
			ReportBuilderUtil.setupDefaultGrid(itemBand, detailElement);

			final String id = "wizard::details-" + field.getId();
			detailElement.setName(id);
			detailElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
			
			if (Boolean.TRUE.equals(detailElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
					AttributeNames.Wizard.ALLOW_METADATA_STYLING))) {
				detailElement.setAttribute("http://reporting.pentaho.org/namespaces/engine/attributes/wizard",
						"CachedWizardFormatData", field);
				detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", field);
			}
			if (Boolean.TRUE.equals(detailElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
					AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
				detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
			}
			
			detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING,
					false);
			detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES,
					false);
			
			detailElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.FORMAT_STRING,
					field.getDataFormat());
			
			String uid = RPT_DETAILS + j   + "-" + INNERMOST + "-" + INNERMOST;

			String htmlClass = "saiku " + uid;
			
			detailElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, htmlClass);
			detailElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, uid);
			
			ElementFormat format = upsertFormatDefinition(INNERMOST, INNERMOST, field);
			
			MergeFormatUtil.mergeElementFormats(detailElement, format);

			itemBand.addElement(detailElement);

			if (Boolean.TRUE.equals(field.isHideRepeating())) {
				detailElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ONLY_SHOW_CHANGING_VALUES,
						Boolean.TRUE);
			}

//			//The Details Header
//			
//			if (detailsHeader != null) {
//				final Element headerElement = AutoGeneratorUtility.generateHeaderElement(field.getId());
//				ReportBuilderUtil.setupDefaultGrid(detailsHeader, headerElement);
//				headerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
//
//				if (Boolean.TRUE.equals(headerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
//						AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
//					headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
//				}
//
//				headerElement.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE,
//						field.getName());
//
//				headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_STYLING,
//						false);
//				headerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES,
//						false);
//				
//				String hdUid = RPT_DETAILS_HEADER + j;
//
//				String hdHtmlClass = "saiku col-header " + hdUid;
//				
//				headerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.STYLE_CLASS, hdHtmlClass);
//				headerElement.setAttribute(AttributeNames.Html.NAMESPACE, AttributeNames.Html.XML_ID, hdUid);
//
//				ElementFormat headerFormat = field.getHeaderFormat();
//
//				if(headerFormat==null){
//					headerFormat = new ElementFormat();
//					field.setHeaderFormat(headerFormat);
//				}
//				
//				MergeFormatUtil.mergeElementFormats(headerElement, headerFormat);
//				
//				detailsHeader.addElement(headerElement);
//			}
			
			//The Details Footer

			if (detailsFooter != null) {
				final Class aggFunctionClass = field.getAggregationFunction().getClassObject();

				final Element footerElement = AutoGeneratorUtility.generateFooterElement(aggFunctionClass,
						ReportBuilderUtil.computeElementType(field.getId(), flowController), null, field.getId());

				ReportBuilderUtil.setupDefaultGrid(detailsFooter, footerElement);

				footerElement.getStyle().setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(width));
				if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
						AttributeNames.Wizard.ALLOW_METADATA_STYLING))) {
					footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFormatData", field);
				}
				if (Boolean.TRUE.equals(footerElement.getAttribute(AttributeNames.Wizard.NAMESPACE,
						AttributeNames.Wizard.ALLOW_METADATA_ATTRIBUTES))) {
					footerElement.setAttribute(AttributeNames.Wizard.NAMESPACE, "CachedWizardFieldData", field);
				}

				//this will be hidden most of the times
				detailsFooter.addElement(footerElement);
			}
			
			CalculatedColumnBuilder calcColumnBuilder = new CalculatedColumnBuilder();
			calcColumnBuilder.build(definition, reportSpecification);
			
		}

	@Override
	protected void processElement(ReportElement element, int i) {

	}

}
