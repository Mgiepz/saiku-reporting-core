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

import java.awt.Color;
import java.awt.Image;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.Band;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.MetaAttributeNames;
import org.pentaho.reporting.engine.classic.core.filter.types.ContentFieldType;
import org.pentaho.reporting.engine.classic.core.filter.types.DateFieldType;
import org.pentaho.reporting.engine.classic.core.filter.types.NumberFieldType;
import org.pentaho.reporting.engine.classic.core.filter.types.TextFieldType;
import org.pentaho.reporting.engine.classic.core.function.ProcessingContext;
import org.pentaho.reporting.engine.classic.core.metadata.ElementType;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.style.BorderStyle;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.engine.classic.core.wizard.AutoGeneratorUtility;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributeContext;
import org.pentaho.reporting.engine.classic.core.wizard.DataAttributes;
import org.pentaho.reporting.engine.classic.core.wizard.DefaultDataAttributeContext;
import org.saiku.reporting.core.model.Length;
import org.saiku.reporting.core.model.FieldDefinition;

public class ReportBuilderUtil {
	
	private static final int MIN_WIDTH = 1;

	private static final Log logger = LogFactory.getLog(ReportBuilderUtil.class);

	protected static ElementType computeElementType(final String fieldId, 
			DefaultFlowController flowController)
	{

		final DataAttributes attributes = flowController.getDataSchema().getAttributes(fieldId);

		final ProcessingContext reportContext = flowController.getReportContext();
		DataAttributeContext attributeContext = new DefaultDataAttributeContext(reportContext.getOutputProcessorMetaData(),
				reportContext.getResourceBundleFactory().getLocale());

		if (attributes == null)
		{
			logger.warn("Field '" + fieldId + "' is declared in the report-specification, " +
			"but not present in the data. Assuming defaults.");
			return new TextFieldType();
		}

		final Class fieldType = (Class) attributes.getMetaAttribute
		(MetaAttributeNames.Core.NAMESPACE, MetaAttributeNames.Core.TYPE, Class.class, attributeContext);
		if (fieldType == null)
		{
			return new TextFieldType();
		}

		if (Number.class.isAssignableFrom(fieldType))
		{
			return new NumberFieldType();
		}
		if (Date.class.isAssignableFrom(fieldType))
		{
			return new DateFieldType();
		}
		if (byte[].class.isAssignableFrom(fieldType) ||
				Blob.class.isAssignableFrom(fieldType) ||
				Image.class.isAssignableFrom(fieldType))
		{
			return new ContentFieldType();
		}
		return new TextFieldType();
	}

	public static Band findGeneratedContent(Band band) {
		//bitte später ersetzen bzw. code kopieren 
		return AutoGeneratorUtility.findGeneratedContent(band);
	}

    public static Band findGeneratedContentById(Band band) {
        //bitte später ersetzen bzw. code kopieren
        return AutoGeneratorUtility.findGeneratedContent(band);
    }

	public static ContentFieldType findChartContent(Band band) {
		return null;
		//return new Content;
		//bitte später ersetzen bzw. code kopieren 
		//return ReportBuilderUtil.findGeneratedContent(band);
	}
	
	/**
	 * @param definition 
	 * @param detailFieldDefinitions
	 * @return
	 */
	public static float[] correctFieldWidths(final ArrayList<FieldDefinition> detailFields,  AbstractReportDefinition  definition) {
		final Float[] widthSpecs = new Float[detailFields.size()];

		Float userDefinedWidths = Float.valueOf(0);
		int numberOfUnsetWidths = 0;
		for (int i = 0; i < detailFields.size(); i++) {
			final FieldDefinition fieldDefinition = detailFields.get(i);
			Length length = null;
			if(fieldDefinition.getElementFormats()!=null &&
			   fieldDefinition.getElementFormats().get(LayoutConstants.INNERMOST)!=null &&
			   fieldDefinition.getElementFormats().get(LayoutConstants.INNERMOST).get(LayoutConstants.INNERMOST)!=null
			){
			  length = fieldDefinition.getElementFormats().get(LayoutConstants.INNERMOST).get(LayoutConstants.INNERMOST).getWidth();
			}
			if (length == null) {
				widthSpecs[i] = null;
				numberOfUnsetWidths++;
				continue;
			}
			widthSpecs[i] = length.getNormalizedValue();
			userDefinedWidths += widthSpecs[i];
		}

		if(userDefinedWidths - (numberOfUnsetWidths * MIN_WIDTH) < -100){
			Float diff = -100 - (userDefinedWidths - (numberOfUnsetWidths * MIN_WIDTH));
			for (int i = detailFields.size() -1; i > 0; i--) {
				if(!(widthSpecs[i]==null)){
					widthSpecs[i] += diff;
				}
			}
		}
		
		final float[] computedWidth = computeFieldWidths(widthSpecs, definition.getPageDefinition()
				.getWidth());
				
		//if sum is now < 100% we need to resize the last one again

		float total = 0;
		
		for (int i = 0; i < computedWidth.length; i++) {
			total+=computedWidth[i];
			logger.debug("width: " + computedWidth[i]);
			
		}
		logger.debug(total);
		
		return computedWidth;
	}
	
	  /**
	   * Computes a set of field widths. The input-width definitions can be a mix of absolute and relative values; the
	   * resulting widths are always relative values. If the input width is null or zero, it is assumed that the field wants
	   * to have a generic width.
	   *
	   * @param fieldDescriptions
	   * @param pageWidth
	   * @return
	   */
	  public static float[] computeFieldWidths(final Float[] fieldDescriptions, final float pageWidth)
	  {
	    final float[] resultWidths = new float[fieldDescriptions.length];

	    float definedWidth = 0;
	    int definedNumberOfFields = 0;
	    for (int i = 0; i < fieldDescriptions.length; i++)
	    {
	      final Number number = fieldDescriptions[i];
	      if (number != null && number.floatValue() != 0)
	      {
	        if (number.floatValue() < 0)
	        {
	          // a fixed value ..
	          resultWidths[i] = number.floatValue();
	          definedNumberOfFields += 1;
	          definedWidth += number.floatValue();
	        }
	        else
	        {
	          final float absValue = number.floatValue();
	          final float relativeValue = -absValue * 100 / pageWidth;
	          resultWidths[i] = relativeValue;
	          definedNumberOfFields += 1;
	          definedWidth += relativeValue;
	        }
	      }
	    }

	    if (definedNumberOfFields == fieldDescriptions.length)
	    {
	      // we are done, all fields are defined.
	      return resultWidths;
	    }


	    if (definedNumberOfFields == 0)
	    {
	      // the worst case, no element provides a weight ..
	      // therefore all fields have the same proportional width.
	      Arrays.fill(resultWidths, -(100 / fieldDescriptions.length));
	      return resultWidths;
	    }

	    final float availableSpace = -100 - definedWidth;
	    if (availableSpace > 0)
	    {
	      // all predefined fields already fill the complete page. There is no space left for the
	      // extra columns.
	      return resultWidths;
	    }

	    final float avgSpace = availableSpace / (fieldDescriptions.length - definedNumberOfFields);
	    for (int i = 0; i < resultWidths.length; i++)
	    {
	      final float width = resultWidths[i];
	      if (width == 0)
	      {
	        resultWidths[i] = avgSpace;
	      }
	    }
	    return resultWidths;
	  }

	public static void setupDefaultGrid(Band band, Element detailElement) {
			setupDefaultPadding(band, detailElement);
			
			final ElementStyleSheet styleSheet = detailElement.getStyle();
			
			// Always make the height of the detailElement dynamic to the band
			// According to thomas negative numbers equate to percentages
			styleSheet.setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(-100));

			final Object maybeBorderStyle = band.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.GRID_STYLE);
			final Object maybeBorderWidth = band.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.GRID_WIDTH);
			final Object maybeBorderColor = band.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.GRID_COLOR);

			if (!(maybeBorderColor instanceof Color && maybeBorderStyle instanceof BorderStyle
					&& maybeBorderWidth instanceof Number)) {
				return;
			}

			final BorderStyle style = (BorderStyle) maybeBorderStyle;
			final Color color = (Color) maybeBorderColor;
			final Number number = (Number) maybeBorderWidth;
			final Float width = new Float(number.floatValue());

			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_TOP_WIDTH, width);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_TOP_COLOR, color);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_TOP_STYLE, style);

			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_LEFT_WIDTH, width);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_LEFT_COLOR, color);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_LEFT_STYLE, style);

			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_WIDTH, width);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_COLOR, color);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_STYLE, style);

			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_RIGHT_WIDTH, width);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_RIGHT_COLOR, color);
			styleSheet.setStyleProperty(ElementStyleKeys.BORDER_RIGHT_STYLE, style);
		}

	private static void setupDefaultPadding(final Band band, final Element detailElement) {
		final Object maybePaddingTop = band.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.PADDING_TOP);
		final Object maybePaddingLeft = band.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.PADDING_LEFT);
		final Object maybePaddingBottom = band.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.PADDING_BOTTOM);
		final Object maybePaddingRight = band.getAttribute(AttributeNames.Wizard.NAMESPACE,
				AttributeNames.Wizard.PADDING_RIGHT);

		if (!(maybePaddingTop instanceof Number && maybePaddingLeft instanceof Number
				&& maybePaddingBottom instanceof Number && maybePaddingRight instanceof Number)) {
			return;
		}

		final Number paddingTop = (Number) maybePaddingTop;
		final Number paddingLeft = (Number) maybePaddingLeft;
		final Number paddingBottom = (Number) maybePaddingBottom;
		final Number paddingRight = (Number) maybePaddingRight;

		final ElementStyleSheet styleSheet = detailElement.getStyle();
		styleSheet.setStyleProperty(ElementStyleKeys.PADDING_TOP, new Float(paddingTop.floatValue()));
		styleSheet.setStyleProperty(ElementStyleKeys.PADDING_LEFT, new Float(paddingLeft.floatValue()));
		styleSheet.setStyleProperty(ElementStyleKeys.PADDING_BOTTOM, new Float(paddingBottom.floatValue()));
		styleSheet.setStyleProperty(ElementStyleKeys.PADDING_RIGHT, new Float(paddingRight.floatValue()));
	}

	public static Element generateFooterElement(Class aggregationType, 
			ElementType targetType, String group, String fieldName) {
		
		Element element = AutoGeneratorUtility.generateFooterElement(aggregationType, targetType, group, fieldName);
		element.setAttribute(AttributeNames.Wizard.NAMESPACE,
		          AttributeNames.Wizard.ALLOW_METADATA_STYLING, Boolean.FALSE);
		
		return element;
	}


}
