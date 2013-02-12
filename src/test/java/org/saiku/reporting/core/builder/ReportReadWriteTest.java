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
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
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

		masterReport.setAttribute(AttributeNames.Wizard.NAMESPACE, "saiku-report-spec", spec);

		final ByteArrayOutputStream prptContent = new ByteArrayOutputStream();
		try {
			BundleWriter.writeReportToZipStream(masterReport, prptContent);		
			OutputStream outputStream = new FileOutputStream ("c:/tmp/dert.prpt");
			prptContent.writeTo(outputStream);	
		
			ResourceManager manager = new ResourceManager();
			manager.registerDefaults();
			Resource res = manager.createDirectly(
				new URL("file:C:/tmp/dert.prpt"), MasterReport.class);
			MasterReport report = (MasterReport) res.getResource();
			
			ReportSpecification spec2 = (ReportSpecification) report.getAttribute(AttributeNames.Wizard.NAMESPACE, "saiku-report-spec");
			
			System.out.println(spec2.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		


	}


}
