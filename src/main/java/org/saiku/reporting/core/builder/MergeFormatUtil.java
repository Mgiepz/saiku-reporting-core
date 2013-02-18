/*
 * Copyright (C) 2011 Marius Giepz
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.saiku.reporting.core.builder;

import java.awt.Color;
import java.awt.Insets;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.PageDefinition;
import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.SimplePageDefinition;
import org.pentaho.reporting.engine.classic.core.layout.style.SimpleStyleSheet;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleSheet;
import org.pentaho.reporting.engine.classic.core.style.TextStyleKeys;
import org.pentaho.reporting.engine.classic.core.style.resolver.SimpleStyleResolver;
import org.pentaho.reporting.engine.classic.core.util.PageFormatFactory;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.PageSetup;
import org.saiku.reporting.core.model.ReportSpecification;

/**
 * This class is used to extract default formatting for a new element from the
 * current template
 * 
 * 
 * @author mgiepz
 * 
 */
public class MergeFormatUtil {

	protected static Log log = LogFactory.getLog(MergeFormatUtil.class);

	/*
	 * Copies all element that are not null from the source to the target
	 * if it is not null in the target
	 */
//	public static void mergeElementFormats(ElementFormat source, ElementFormat target) throws Exception {
//
//		BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
//
//		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
//
//			if (descriptor.getWriteMethod() != null) {
//
//				Object sourceValue = descriptor.getReadMethod().invoke(
//						source);
//				if (sourceValue != null) {
//					descriptor.getWriteMethod().invoke(target, sourceValue);
//				}
//
//			}
//		}
//		
//	}



	/**
	 * Copies all element format prptFormation from a real report element to the
	 * Saiku model and vice-versa.
	 * 
	 * @param prptFormat
	 * @param saikuFormat
	 */
	public static void mergeElementFormats(ReportElement detailElement, ElementFormat saikuFormat) {

		SimpleStyleSheet styleSheet = SimpleStyleResolver.resolveOneTime(detailElement); //TODO: cache this
		ElementStyleSheet prptFormat = detailElement.getStyle();
		
		if (prptFormat == null || saikuFormat == null) {
			return;
		}

		if (saikuFormat.getLeftPadding() == null) {
			final Float padding = (Float) styleSheet.getStyleProperty(ElementStyleKeys.PADDING_LEFT, null);
			saikuFormat.setLeftPadding(padding);
		} else {
			prptFormat.setStyleProperty(ElementStyleKeys.PADDING_LEFT,saikuFormat.getLeftPadding());
		}

		if (saikuFormat.getRightPadding() == null) {
			final Float padding = (Float) styleSheet.getStyleProperty(ElementStyleKeys.PADDING_RIGHT, null);
			saikuFormat.setRightPadding(padding);
		} else {
			prptFormat.setStyleProperty(ElementStyleKeys.PADDING_RIGHT,saikuFormat.getRightPadding());
		}

		/*
		 * warum ist das hier negativ
		 */

		if (saikuFormat.getWidth() == null) {
			final Float width = (Float) styleSheet.getStyleProperty(ElementStyleKeys.MIN_WIDTH, null);
//			saikuFormat.setWidth(-width);
		} else {
			// prptFormat.setStyleProperty(ElementStyleKeys.MIN_WIDTH,
			// -(new Float(saikuFormat.getWidth())));
		}

		if (saikuFormat.getBackgroundColor() == null) {
			final Color color = (Color) styleSheet.getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, null);
			saikuFormat.setBackgroundColor(color);
		} else {
			prptFormat.setStyleProperty(ElementStyleKeys.BACKGROUND_COLOR, saikuFormat.getBackgroundColor());
		}

		if (saikuFormat.getFontColor() == null) {
			final Color color = (Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT, null);
			saikuFormat.setFontColor(color);
		} else {
			prptFormat.setStyleProperty(ElementStyleKeys.PAINT, saikuFormat.getFontColor());
		}

		if (saikuFormat.getFontName() == null) {
			final String font = (String) styleSheet.getStyleProperty(TextStyleKeys.FONT, null);
			saikuFormat.setFontName(font);
		} else {
			prptFormat.setStyleProperty(TextStyleKeys.FONT, saikuFormat.getFontName());
		}

		if (saikuFormat.getFontBold() == null) {
			final Boolean fontBold = (Boolean) styleSheet.getStyleProperty(TextStyleKeys.BOLD, null);
			saikuFormat.setFontBold(fontBold);
		} else {
			prptFormat.setStyleProperty(TextStyleKeys.BOLD, saikuFormat.getFontBold());
		}

		if (saikuFormat.getFontItalic() == null) {
			final Boolean fontItalic = (Boolean) styleSheet.getStyleProperty(TextStyleKeys.ITALIC, null);
			saikuFormat.setFontBold(fontItalic);
		} else {
			prptFormat.setStyleProperty(TextStyleKeys.ITALIC, saikuFormat.getFontItalic());
		}

		if (saikuFormat.getFontUnderline() == null) {
			final Boolean fontUnderlined = (Boolean) styleSheet.getStyleProperty(TextStyleKeys.UNDERLINED, null);
			saikuFormat.setFontUnderline(fontUnderlined);
		} else {
			prptFormat.setStyleProperty(TextStyleKeys.UNDERLINED, saikuFormat.getFontUnderline());
		}

		if (saikuFormat.getHorizontalAlignment() == null) {
			final ElementAlignment horz = (ElementAlignment) styleSheet.getStyleProperty(ElementStyleKeys.ALIGNMENT, null);
			saikuFormat.setHorizontalAlignment(horz);
		} else {
			prptFormat.setStyleProperty(ElementStyleKeys.ALIGNMENT,saikuFormat.getHorizontalAlignment());
		}

		if (saikuFormat.getVerticalAlignment() == null) {
			final ElementAlignment vert = (ElementAlignment) styleSheet.getStyleProperty(ElementStyleKeys.VALIGNMENT, null);
			saikuFormat.setVerticalAlignment(vert);
		} else {
			prptFormat.setStyleProperty(ElementStyleKeys.VALIGNMENT,saikuFormat.getVerticalAlignment());
		}

		if (saikuFormat.getFontSize() == null) {
			final Integer size = (Integer) styleSheet.getStyleProperty(TextStyleKeys.FONTSIZE, null);
			if(size != null) saikuFormat.setFontSize(size.intValue());
		} else {
			prptFormat.setStyleProperty(TextStyleKeys.FONTSIZE, new Integer(saikuFormat.getFontSize()));
		}

	}

	public static void mergePageSetup(ReportSpecification model, MasterReport output) {

		Paper paper = null;
		
		PageSetup settings = model.getPageSetup();

		if (settings.getPageFormat() == null) {
			paper = output.getPageDefinition().getPageFormat(0).getPaper();
			settings.setPageFormat(PageFormatFactory.getInstance().getPageFormatName(paper.getWidth(), paper.getHeight()));
		} else {
			paper = PageFormatFactory.getInstance().createPaper(settings.getPageFormat());
		}

		PageFormat pageFormat = null;
		if (settings.getPageOrientation() == null) {
			settings.setPageOrientation(output.getPageDefinition().getPageFormat(0).getOrientation());
			pageFormat = output.getPageDefinition().getPageFormat(0);
		} else {
			int orientation = settings.getPageOrientation();
			pageFormat = PageFormatFactory.getInstance().createPageFormat(paper, orientation);
		}

		if (settings.getBottomMargin() == null || settings.getLeftMargin() == null || settings.getRightMargin() == null
				|| settings.getTopMargin() == null) {
			Insets insets = PageFormatFactory.getInstance().getPageMargins(output.getPageDefinition().getPageFormat(0));
			settings.setBottomMargin(insets.bottom);
			settings.setLeftMargin(insets.left);
			settings.setTopMargin(insets.top);
			settings.setRightMargin(insets.right);
		} else {
			Insets insets = new Insets(settings.getTopMargin(), settings.getLeftMargin(), settings.getBottomMargin(),
					settings.getRightMargin());
			PageFormatFactory.getInstance().setPageMargins(pageFormat, insets);
		}

		PageDefinition format = new SimplePageDefinition(pageFormat);
		output.setPageDefinition(format);

	}
	
}
