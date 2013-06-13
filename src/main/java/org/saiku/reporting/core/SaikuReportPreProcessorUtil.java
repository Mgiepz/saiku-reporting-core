package org.saiku.reporting.core;

import java.util.ArrayList;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.wizard.model.WizardSpecification;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceKeyCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.types.GroupType;

public class SaikuReportPreProcessorUtil {

	public static ReportSpecification loadReportSpecification(MasterReport definition, ResourceManager resourceManager)
			throws ResourceLoadingException, ResourceCreationException,
			ResourceKeyCreationException {

		//Check if allready available
	    final Object maybeSpec = definition.getAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec");
	    
	    if (maybeSpec instanceof ReportSpecification)
	    {
	      return (ReportSpecification) maybeSpec;
	    }
		
	    //If not try, load resource
		final ResourceKey contentBase = definition.getContentBase();
		final ResourceKey resourceKey = resourceManager.deriveKey(contentBase,
				"saiku-report-spec.xml");
		final Resource resource = resourceManager.create(resourceKey,
				contentBase, ReportSpecification.class);

		try {
			return (ReportSpecification) resource.getResource();
		} catch (ResourceException e) {
			return null;
		}

	}

	public static void saveReportSpecification(MasterReport definition,ReportSpecification reportSpecification) {
		definition.setAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec", reportSpecification);
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
