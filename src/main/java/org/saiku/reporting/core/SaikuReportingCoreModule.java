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
