package org.saiku.reporting.core;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceKeyCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
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
import org.saiku.reporting.core.model.TemplateDefinition;
import org.saiku.reporting.core.model.types.GroupType;

public class SaikuReportPreProcessorUtil {

	public static ReportSpecification loadReportSpecification(MasterReport definition, ResourceManager resourceManager)
			throws ResourceLoadingException, ResourceCreationException,
			ResourceKeyCreationException {

		//Check if allready available
		final Object maybeSpec = definition.getAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec");

		String spec = null;

		if (maybeSpec instanceof String)
		{
			spec = (String) maybeSpec;
		}

		if(spec==null){

			//If not try, load resource
			final ResourceKey contentBase = definition.getContentBase();
			final ResourceKey resourceKey = resourceManager.deriveKey(contentBase,
					"saiku-report-spec.xml");
			final Resource resource = resourceManager.create(resourceKey,
					contentBase, ReportSpecification.class);

			try {
				spec = (String) resource.getResource();
			} catch (ResourceException e) {
				throw new IllegalArgumentException("no reportspec found");
			}

		}

		JAXBContext jc;

		StringReader reader = new StringReader(spec);

		ReportSpecification reportSpecification = null;

		try {

			jc = getJAXBContext();

			Unmarshaller u = jc.createUnmarshaller();

			reportSpecification = (ReportSpecification) u.unmarshal(reader);

		} catch (JAXBException e) {

			e.printStackTrace();
		} 

		return reportSpecification;

	}

	public static void saveReportSpecification(MasterReport definition,ReportSpecification reportSpecification) throws BundleWriterException {

		StringWriter sw = new StringWriter();

		JAXBContext jc;
		try {
			jc = getJAXBContext();

			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(reportSpecification, sw);

		} catch (JAXBException e) {
			throw new BundleWriterException("Failed to write saiku-report-specifiation",e);
		}

		definition.setAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec", sw.toString());
	}

	private static JAXBContext getJAXBContext() throws JAXBException {
		return JAXBContext.newInstance(
				RootBandFormat.class,
				ElementFormat.class,
				FieldDefinition.class,
				GroupDefinition.class,
				Chart.class,
				Label.class,
				DataSource.class,
				PageSetup.class,
				ReportSpecification.class,
				Length.class,
				LengthUnit.class,
				TemplateDefinition.class
				);
	}

	public static boolean isCrosstab(ReportSpecification reportSpecification) {

		ArrayList<GroupDefinition> groupDefinitions = reportSpecification
				.getGroupDefinitions();

		for (GroupDefinition groupDefinition : groupDefinitions) {
			GroupType type = groupDefinition.getType();
			if (GroupType.CT_COLUMN.equals(type)
					|| GroupType.CT_ROW.equals(type)) {
				return true;
			}
		}
		return false;
	}
}
