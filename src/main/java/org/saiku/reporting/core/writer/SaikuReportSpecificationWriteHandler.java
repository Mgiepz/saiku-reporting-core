package org.saiku.reporting.core.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterState;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentBundle;
import org.saiku.reporting.core.SaikuReportingCoreModule;
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

public class SaikuReportSpecificationWriteHandler implements
		BundleWriterHandler {

	@Override
	public String writeReport(WriteableDocumentBundle bundle,
			BundleWriterState state) throws IOException, BundleWriterException {

		ReportSpecification reportSpecification = null; //wo kriegen wir die her?
		
	    final Object maybeSpec = state.getReport().getAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec");
	    
	    if (maybeSpec instanceof ReportSpecification)
	    {
	    	reportSpecification = (ReportSpecification) maybeSpec;
	    }

	    final BundleWriterState wizardFileState = new BundleWriterState(state, "saiku-report-spec.xml");   
	    final OutputStream outputStream = new BufferedOutputStream(bundle.createEntry(wizardFileState.getFileName(), "text/xml"));
	    //final DefaultTagDescription tagDescription = new DefaultTagDescription();
	    //tagDescription.setNamespaceHasCData(SaikuReportingCoreModule.NAMESPACE, false);
	    
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(
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
					LengthUnit.class	
					);

			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(reportSpecification, outputStream);
			
			
			outputStream.flush();
			outputStream.close();
			
		    return wizardFileState.getFileName();

		} catch (JAXBException e) {
			throw new BundleWriterException("Failed to write saiku-report-specifiation",e);
		}

	}

	@Override
	public int getProcessingOrder() {
		return 100001;
	}

}
