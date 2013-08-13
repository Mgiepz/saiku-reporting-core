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
