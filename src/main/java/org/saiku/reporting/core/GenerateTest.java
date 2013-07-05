package org.saiku.reporting.core;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.function.ProcessingContext;
import org.pentaho.reporting.engine.classic.core.layout.output.AbstractReportProcessor;
import org.pentaho.reporting.engine.classic.core.layout.output.DefaultProcessingContext;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.NamedStaticDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.base.StreamReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.AllItemsHtmlPrinter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.FileSystemURLRewriter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlPrinter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.StreamHtmlOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriter;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.util.ReportParameterValues;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.DefaultNameGenerator;
import org.pentaho.reporting.libraries.repository.stream.StreamRepository;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceKeyCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class GenerateTest{

	public static void main(String[] args) throws Exception {
		
		final MasterReport masterReport = doMain();
		
		testComponent(masterReport);
		
		
		
//		masterReport.copyInto(arg0);
//		
//	    bundle.getWriteableDocumentMetaData().setBundleType("text/plain");
//	    final OutputStream outputStream = bundle.createEntry("saiku-report.xml", "text/plain");
//	    p1.store(outputStream, "run 1");
//	    outputStream.close();
		
//		irgendwie so
//		masterReport.setAttribute(AttributeNames.Wizard.NAMESPACE,
//				"wizard-spec", wizardSpecification);
	

//		generateReportPdf(masterReport);
//		generateReportHtml(masterReport);
		
		//storeReport(masterReport);
		
		System.out.println("done!");
		
	}

	private static MasterReport doMain() throws ResourceLoadingException,
			ResourceCreationException, ResourceKeyCreationException,
			MalformedURLException, ResourceException,
			ReportDataFactoryException, ReportProcessingException {
		ClassicEngineBoot.getInstance().start();
		
		final Log logger = LogFactory.getLog(GenerateTest.class);

		logger.info("test");
		
		
		// load report definition
		ResourceManager manager = new ResourceManager();
		manager.registerDefaults();
		Resource res = manager.createDirectly(
			new URL("file:C:/tmp/cobalt_4_left_aligned_grid.prpt"), MasterReport.class);
		MasterReport report = (MasterReport) res.getResource();
		report.setDataFactory(getDataFactory());
		report.setQuery("MASTER_QUERY");
		
		final ProcessingContext processingContext = new DefaultProcessingContext();
		final DefaultFlowController flowController = new DefaultFlowController
		(processingContext, report.getDataSchemaDefinition(), new ReportParameterValues());


		final SaikuReportPreProcessor processor = new SaikuReportPreProcessor();
		final MasterReport masterReport = processor.performPreProcessing(report, flowController);
		return masterReport;
	}
	
	public static String render(){
		
		try {
			final MasterReport masterReport = doMain();
			
			return generateReportHtml(masterReport);
			
		} catch (Exception e) {
		}
		
		return null;
	};

	public static void storeReport(MasterReport masterReport) {

		final ByteArrayOutputStream prptContent = new ByteArrayOutputStream();
		try {
			BundleWriter.writeReportToZipStream(masterReport, prptContent);
			
			OutputStream outputStream = new FileOutputStream ("c:/tmp/xtab.prpt");
			prptContent.writeTo(outputStream);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BundleWriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ContentIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	private static DataFactory getDataFactory(){
		NamedStaticDataFactory factory = new NamedStaticDataFactory();
		factory.setQuery("MASTER_QUERY", "org.saiku.reporting.core.builder.TestDataFactory#getAllData");
		return factory;
	}

	protected static MasterReport parseReport(final File file) throws ResourceException
	{
		final ResourceManager manager = new ResourceManager();
		manager.registerDefaults();
		final Resource resource = manager.createDirectly(file, MasterReport.class);
		return (MasterReport) resource.getResource();
	}
	
	public static void testComponent(MasterReport report) throws Exception{
		
		String outputFile = "C:/tmp/test.html";
		// Open the output stream
		
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(
				outputFile ));
		
//		final StandaloneReportingComponent pentahoReportingPlugin = new StandaloneReportingComponent();
//		
//		pentahoReportingPlugin.setReport(report);
//		pentahoReportingPlugin.setPaginateOutput(true);
//		pentahoReportingPlugin.setInputs(null);
//		pentahoReportingPlugin.setDefaultOutputTarget(HtmlTableModule.TABLE_HTML_PAGE_EXPORT_TYPE);
//		pentahoReportingPlugin.setOutputTarget(HtmlTableModule.TABLE_HTML_PAGE_EXPORT_TYPE);
//		pentahoReportingPlugin.setDashboardMode(false);
//		pentahoReportingPlugin.setOutputStream(outputStream);
//		pentahoReportingPlugin.setAcceptedPage(0);
//		pentahoReportingPlugin.validate();
//		pentahoReportingPlugin.execute();
		
	}
	
	
	private static String generateReportHtml(MasterReport report) throws FileNotFoundException, ReportProcessingException{

		// Prepare to generate the report
		AbstractReportProcessor reportProcessor = null;
		try {
			
			String outputFile = "C:/tmp/test.html";
			// Open the output stream
//			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(
//					outputFile ));
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
				final StreamRepository targetRepository = new StreamRepository(outputStream);
				final ContentLocation targetRoot = targetRepository.getRoot();
				final HtmlOutputProcessor outputProcessor = new StreamHtmlOutputProcessor(report.getConfiguration());
				
				
				final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
				printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, "index", "html"));
				
				printer.setDataWriter(null, null);
				printer.setUrlRewriter(new FileSystemURLRewriter());
				outputProcessor.setPrinter(printer);
				reportProcessor = new StreamReportProcessor(report, outputProcessor);

			// Generate the report
			reportProcessor.processReport();
			
			return outputStream.toString();
			
			
		} finally {
			if (reportProcessor != null) {
				reportProcessor.close();
			}
		}
	}

	
	private static void generateReportPdf(MasterReport report) throws FileNotFoundException, ReportProcessingException{

		// Prepare to generate the report
		AbstractReportProcessor reportProcessor = null;
		try {
			
			String outputFile = "C:/tmp/test.pdf";
			// Open the output stream
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(
					outputFile ));
			
			final PdfOutputProcessor outputProcessor = new PdfOutputProcessor(
					report.getConfiguration(), outputStream, report
							.getResourceManager());
			reportProcessor = new PageableReportProcessor(report,
					outputProcessor);

			// Generate the report
			reportProcessor.processReport();
		} finally {
			if (reportProcessor != null) {
				reportProcessor.close();
			}
		}
	}
}
