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
package org.saiku.reporting.core.parser;

import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.SimpleResource;
import org.pentaho.reporting.libraries.resourceloader.factory.AbstractResourceFactory;
import org.saiku.reporting.core.model.ReportSpecification;

public class SaikuReportSpecificationResourceFactory extends AbstractResourceFactory{

	public SaikuReportSpecificationResourceFactory() {
		super(ReportSpecification.class);
	}

	@Override
	public Resource create(ResourceManager resourceManager, ResourceData resourceData,
			ResourceKey resourceKey) throws ResourceCreationException,
			ResourceLoadingException {

		String reportSpecification = new String( resourceData.getResource(resourceManager));

		return new SimpleResource(resourceKey, reportSpecification, String.class, 1);

	}

}
