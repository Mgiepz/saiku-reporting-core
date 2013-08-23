package org.saiku.reporting.core;

import java.util.ArrayList;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.cache.CachingDataFactory;
import org.pentaho.reporting.engine.classic.core.function.ProcessingContext;
import org.pentaho.reporting.engine.classic.core.function.ProcessingDataFactoryContext;
import org.pentaho.reporting.engine.classic.core.function.StructureFunction;
import org.pentaho.reporting.engine.classic.core.layout.output.DefaultProcessingContext;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterDefinitionEntry;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.util.ReportParameterValues;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchemaDefinition;
import org.pentaho.reporting.engine.classic.wizard.WizardOverrideFormattingFunction;
import org.pentaho.reporting.engine.classic.wizard.WizardProcessor;
import org.saiku.reporting.core.builder.ParameterDefinitionBuilder;
import org.saiku.reporting.core.model.ReportSpecification;

/**
 * This is the main Object of the Saiku Reporting Core
 * @author mg
 *
 */
public class SaikuReportProcessor {

	/**
	 * This method applies a ReportSpecification (The Saiku-Layout-Model)
	 * to a prpt template and lets the Pentaho-Reporting-Engine run the
	 * SaikuReportPreProcessor. The Result is a new MasterReport, that has 
	 * the Layout described in the Saiku-Layout-Model
	 * 
	 * @param template
	 * @param spec
	 * @return
	 * @throws BundleWriterException 
	 */
	public MasterReport preProcessReport(MasterReport reportTemplate, ReportSpecification spec) throws BundleWriterException{
		
		SaikuReportPreProcessorUtil.saveReportSpecification(reportTemplate, spec);
		
		CachingDataFactory dataFactory = null;
		try {

//			ArrayList<String> queryIds = new ArrayList<String>();

//			reportTemplate.setDataFactory(null); //TODO:
//			reportTemplate.setQuery("MASTER_QUERY");

			ParameterDefinitionBuilder paramBuilder = new ParameterDefinitionBuilder();
			reportTemplate.setParameterDefinition(paramBuilder.build(spec));

			final ProcessingContext processingContext = new DefaultProcessingContext();
			final DataSchemaDefinition definition = reportTemplate.getDataSchemaDefinition();

			ReportParameterValues parameterValues = new ReportParameterValues();
//					StateUtilities.computeParameterValueSet(reportTemplate,
//					//getReportParameterValues(model)
//					null
//					);

			final ParameterDefinitionEntry[] parameterDefinitions = reportTemplate.getParameterDefinition()
			.getParameterDefinitions();

			//müssen die Parameter definitions nichtmehr hier rein???
			final DefaultFlowController flowController = new DefaultFlowController
					(processingContext, reportTemplate.getDataSchemaDefinition(), parameterValues);

			ensureSaikuPreProcessorIsAdded(reportTemplate);
			ensureHasOverrideWizardFormatting(reportTemplate, flowController);

			dataFactory = new CachingDataFactory(
					reportTemplate.getDataFactory(), false);

			dataFactory.initialize(new ProcessingDataFactoryContext(processingContext, dataFactory));

			DefaultFlowController postQueryFlowController = flowController
			.performQuery(dataFactory, reportTemplate.getQuery(),
					reportTemplate.getQueryLimit(), reportTemplate
					.getQueryTimeout(), flowController
					.getMasterRow().getResourceBundleFactory());

			reportTemplate.setAttribute(AttributeNames.Wizard.NAMESPACE,
					AttributeNames.Wizard.ENABLE, Boolean.TRUE);

	
			//warum müssen wir das hier überhaupt noch aufrufen? 
			//der report sollte das doch selbst regeln
			
			SaikuReportPreProcessor processor = new SaikuReportPreProcessor();
			processor.setReportSpecification(spec);
			
			MasterReport output = processor.performPreProcessing(
					reportTemplate, postQueryFlowController);

			output.setAttribute(AttributeNames.Wizard.NAMESPACE,
					AttributeNames.Wizard.ENABLE, Boolean.FALSE);

			//TemplateUtils.mergePageSetup(model, output);
	
			ensureSaikuReportPreProcessorIsRemoved(output);
			
			//get back the enriched model
			spec = processor.getReportSpecification();
			
			return output;

		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(dataFactory!=null){
				dataFactory.close();
			}
		}

		return reportTemplate;

	}

	/**
	 * Remove a SaikuReportPreProcessor from the report in case it has one
	 * @param report
	 */
	private void ensureSaikuReportPreProcessorIsRemoved(final AbstractReportDefinition report) {

		final ReportPreProcessor[] oldProcessors = report.getPreProcessors();

		ArrayList<ReportPreProcessor> newProcessors = new ArrayList<ReportPreProcessor>();

		for (int i = 0; i < oldProcessors.length; i++)
		{
			ReportPreProcessor processor = oldProcessors[i];
			if (!(processor instanceof SaikuReportPreProcessor || processor instanceof WizardProcessor))
			{
				newProcessors.add(processor);
			}
		}

		final ReportPreProcessor[] array = newProcessors.toArray(new ReportPreProcessor[newProcessors.size()]);
		report.setAttribute(AttributeNames.Internal.NAMESPACE, AttributeNames.Internal.PREPROCESSORS, array);

	}

	protected static void ensureSaikuPreProcessorIsAdded(final AbstractReportDefinition report)
	{
		final ReportPreProcessor[] processors = report.getPreProcessors();
		boolean hasSaikuProcessor = false;
		for (int i = 0; i < processors.length; i++)
		{
			final ReportPreProcessor processor = processors[i];
			if (processor instanceof SaikuReportPreProcessor)
			{
				hasSaikuProcessor = true;
			}
		}
		if (!hasSaikuProcessor)
		{
			//Add a new processor with the current model
			final SaikuReportPreProcessor processor = new SaikuReportPreProcessor();
			report.addPreProcessor(processor);
		}
	}

	protected static void ensureHasOverrideWizardFormatting(
			AbstractReportDefinition reportTemplate,
			DefaultFlowController flowController) {

		final StructureFunction[] structureFunctions = reportTemplate.getStructureFunctions();

		boolean hasOverrideWizardFormatting = false;

		for (int i = 0; i < structureFunctions.length; i++) {
			final StructureFunction structureFunction = structureFunctions[i];
			if(structureFunction instanceof WizardOverrideFormattingFunction){
				hasOverrideWizardFormatting = false;
				break;
			}
		}
		if(!hasOverrideWizardFormatting){
			reportTemplate.addStructureFunction(new WizardOverrideFormattingFunction());
		}

	}

}
