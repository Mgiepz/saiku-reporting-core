/**
 * 
 */
package org.saiku.reporting.core.parser;

import org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo;
import org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;

/**
 * @author mg
 *
 */
public class SaikuReportSpecificationXmlFactoryModule implements
		XmlFactoryModule {

	/* (non-Javadoc)
	 * @see org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule#getDocumentSupport(org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo)
	 */
	@Override
	public int getDocumentSupport(XmlDocumentInfo documentInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule#createReadHandler(org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo)
	 */
	@Override
	public XmlReadHandler createReadHandler(XmlDocumentInfo documentInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule#getDefaultNamespace(org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo)
	 */
	@Override
	public String getDefaultNamespace(XmlDocumentInfo documentInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
