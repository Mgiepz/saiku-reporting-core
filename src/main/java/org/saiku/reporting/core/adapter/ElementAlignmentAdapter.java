/**
 * 
 */
package org.saiku.reporting.core.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.util.beans.ElementAlignmentValueConverter;

/**
 * @author mg
 *
 */
public abstract class ElementAlignmentAdapter extends XmlAdapter<String,ElementAlignment> {
	
		public abstract ElementAlignment unmarshal(String s) throws Exception;
		
		public String marshal(ElementAlignment c) throws Exception {
			final ElementAlignmentValueConverter con = new ElementAlignmentValueConverter();
			return con.toAttributeValue(c);
		}
	}