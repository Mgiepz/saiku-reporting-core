package org.saiku.reporting.core.parser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.SimpleResource;
import org.pentaho.reporting.libraries.resourceloader.factory.AbstractResourceFactory;
import org.saiku.reporting.core.model.Chart;
import org.saiku.reporting.core.model.DataSource;
import org.saiku.reporting.core.model.ElementFormat;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.Label;
import org.saiku.reporting.core.model.Length;
import org.saiku.reporting.core.model.LengthUnit;
import org.saiku.reporting.core.model.PageSetup;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.RootBandFormat;

public class SaikuReportSpecificationResourceFactory extends AbstractResourceFactory{

	public SaikuReportSpecificationResourceFactory() {
		super(ReportSpecification.class);
	}

	@Override
	public Resource create(ResourceManager resourceManager, ResourceData resourceData,
			ResourceKey resourceKey) throws ResourceCreationException,
			ResourceLoadingException {

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
					//Parameter.class,
					//TemplateDefinition.class,
					ReportSpecification.class,
					RootBandFormat.class
					);
			
			Unmarshaller u = jc.createUnmarshaller();
			ReportSpecification reportSpecification = (ReportSpecification) u.unmarshal(resourceData.getResourceAsStream(resourceManager)); 
			
			return new SimpleResource(resourceKey, reportSpecification, ReportSpecification.class, 1);

		} catch (JAXBException e) {
			throw new ResourceCreationException();
		}

	}

}
