/**
 * 
 */
package org.saiku.reporting.core.parser;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.Chart;
import org.saiku.reporting.core.model.DataSource;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.Label;
import org.saiku.reporting.core.model.Length;
import org.saiku.reporting.core.model.LengthUnit;
import org.saiku.reporting.core.model.PageSetup;
import org.saiku.reporting.core.model.Parameter;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.RootBandFormat;
import org.saiku.reporting.core.model.TemplateDefinition;
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
		String xmlString = attrs.getValue(getUri(), "saiku-report-spec");
		
		ByteArrayInputStream input = new ByteArrayInputStream (xmlString.getBytes());
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(
					Chart.class,
					DataSource.class,
					ElementFormat.class,
					FieldDefinition.class,
					GroupDefinition.class,
					Label.class,
					Length.class,
					LengthUnit.class,
					PageSetup.class,
					Parameter.class,
					ReportSpecification.class,
					RootBandFormat.class,
					TemplateDefinition.class	
					);
			
			Unmarshaller u = jc.createUnmarshaller();
			reportSpecification = (ReportSpecification) u.unmarshal(input); 
			
		} catch (JAXBException e) {
			throw new SAXException(e);
		}
	}

	@Override
	public Object getObject() throws SAXException {

		return reportSpecification;
	}

}
