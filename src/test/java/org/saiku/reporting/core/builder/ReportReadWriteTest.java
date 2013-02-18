package org.saiku.reporting.core.builder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriter;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.saiku.reporting.core.SaikuReportingCoreModule;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.ReportSpecification;

public class ReportReadWriteTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		ClassicEngineBoot.getInstance().start();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();

	}

	public void testReadWrite(){

		MasterReport masterReport = new MasterReport();

		ReportSpecification spec = new ReportSpecification();
		ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<FieldDefinition>();
		fieldDefinitions.add(new FieldDefinition());
		spec.setFieldDefinitions(fieldDefinitions );

		masterReport.setAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec", spec);

		final ByteArrayOutputStream prptContent = new ByteArrayOutputStream();
		try {
			BundleWriter.writeReportToZipStream(masterReport, prptContent);		
			OutputStream outputStream = new FileOutputStream ("c:/tmp/dert.prpt");
			prptContent.writeTo(outputStream);	
		
			outputStream.close();
			
			ResourceManager manager = new ResourceManager();
			manager.registerDefaults();
			URL keyValue = new URL("file:C:/tmp/dert.prpt");
			
			Resource res = manager.createDirectly(
				keyValue, MasterReport.class);
			MasterReport report = (MasterReport) res.getResource();
			
			final ResourceKey contentBase = report.getContentBase();
		    final ResourceKey resourceKey = manager.deriveKey(contentBase, "saiku-report-spec.xml");
		    final Resource resource = manager.create(resourceKey, contentBase, ReportSpecification.class);
		    
			
	       // return (ReportSpecification) resource.getResource(); 

			//ReportSpecification spec2 = (ReportSpecification) report.getAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec");
			
			//System.out.println(spec2.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		


	}


}
