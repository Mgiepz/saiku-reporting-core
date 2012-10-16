/**
 * 
 */
package org.saiku.reporting.core.parser;

import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.saiku.reporting.core.model.ReportSpecification;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author mg
 *
 */
public class SaikuReportSpecificationReadHandler extends AbstractXmlReadHandler {

	private ReportSpecification reportSpecification;

	protected void startParsing(final Attributes attrs) throws SAXException
	{
		reportSpecification = new ReportSpecification();
	}



	@Override
	public Object getObject() throws SAXException {
		// TODO Auto-generated method stub
		return null;
	}

}
