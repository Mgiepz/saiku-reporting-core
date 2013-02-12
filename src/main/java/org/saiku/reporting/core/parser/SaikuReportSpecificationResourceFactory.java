package org.saiku.reporting.core.parser;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportResource;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlResourceFactory;
import org.pentaho.reporting.libraries.xmlns.parser.RootXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModule;
import org.pentaho.reporting.libraries.xmlns.parser.XmlFactoryModuleRegistry;
import org.saiku.reporting.core.model.ReportSpecification;

public class SaikuReportSpecificationResourceFactory extends
		AbstractXmlResourceFactory {
	
	private static final XmlFactoryModuleRegistry registry = new XmlFactoryModuleRegistry();

	  public static void register(final Class<? extends XmlFactoryModule> readHandler)
	  {
	    registry.register(readHandler);
	  }

	  public SaikuReportSpecificationResourceFactory()
	  {
	  }

	  public void initializeDefaults()
	  {
	    super.initializeDefaults();
	    final XmlFactoryModule[] registeredHandlers = registry.getRegisteredHandlers();
	    for (int i = 0; i < registeredHandlers.length; i++)
	    {
	      registerModule(registeredHandlers[i]);
	    }
	  }

	  protected Configuration getConfiguration()
	  {
	    return ClassicEngineBoot.getInstance().getGlobalConfig();
	  }

	  public Class getFactoryType()
	  {
	    return ReportSpecification.class;
	  }

	  protected Resource createResource(final ResourceKey targetKey,
	                                    final RootXmlReadHandler handler,
	                                    final Object createdProduct,
	                                    final Class createdType)
	  {
	    return new ReportResource
	        (targetKey, handler.getDependencyCollector(), createdProduct, createdType, true);
	  }

}
