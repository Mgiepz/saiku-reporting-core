package org.saiku.reporting.core;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterHandlerRegistry;
import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;
import org.saiku.reporting.core.writer.SaikuReportSpecificationWriteHandler;

public class SaikuReportingCoreModule extends AbstractModule
{
	
  public static final String NAMESPACE = "http://www.analytical-labs.com/namespaces/saiku-reporting/1.0";

  public SaikuReportingCoreModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public void initialize(final SubSystem subSystem) throws ModuleInitializeException
  {
    BundleWriterHandlerRegistry.getInstance().registerMasterReportHandler(SaikuReportSpecificationWriteHandler.class);
    BundleWriterHandlerRegistry.getInstance().registerSubReportHandler(SaikuReportSpecificationWriteHandler.class);
  }
}
