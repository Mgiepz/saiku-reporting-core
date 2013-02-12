/**
 * 
 */
package org.saiku.reporting.core.parser;

import org.pentaho.reporting.engine.classic.wizard.WizardCoreModule;
import org.pentaho.reporting.libraries.xmlns.parser.XmlDocumentInfo;
import org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;

/**
 * @author mg
 *
 */
public class SaikuReportSpecificationXmlFactoryModule implements
XmlFactoryModule {

	public int getDocumentSupport(final XmlDocumentInfo documentInfo)
	{
		final String rootNamespace = documentInfo.getRootElementNameSpace();
		if (rootNamespace != null && rootNamespace.length() > 0)
		{
			if (WizardCoreModule.NAMESPACE.equals(rootNamespace) == false)
			{
				return NOT_RECOGNIZED;
			}
			else if ("saiku-report-spec".equals(documentInfo.getRootElement()))
			{
				return RECOGNIZED_BY_NAMESPACE;
			}
		}
		else if ("saiku-report-spec".equals(documentInfo.getRootElement()))
		{
			return RECOGNIZED_BY_TAGNAME;
		}

		return NOT_RECOGNIZED;
	}

	public String getDefaultNamespace(final XmlDocumentInfo documentInfo)
	{
		return WizardCoreModule.NAMESPACE;
	}

	public XmlReadHandler createReadHandler(final XmlDocumentInfo documentInfo)
	{
		return new SaikuReportSpecificationReadHandler();
	}

}
