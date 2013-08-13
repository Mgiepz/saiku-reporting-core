package org.saiku.reporting.core.writer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterState;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentBundle;
import org.saiku.reporting.core.SaikuReportingCoreModule;

public class SaikuReportSpecificationWriteHandler implements
BundleWriterHandler {

	@Override
	public String writeReport(WriteableDocumentBundle bundle,
			BundleWriterState state) throws IOException, BundleWriterException {

		String reportSpecification = null; //wo kriegen wir die her?

		final Object maybeSpec = state.getReport().getAttribute(SaikuReportingCoreModule.NAMESPACE, "saiku-report-spec");

		if (maybeSpec instanceof String)
		{
			reportSpecification = (String) maybeSpec;
		}

		final BundleWriterState wizardFileState = new BundleWriterState(state, "saiku-report-spec.xml");   
		final OutputStream outputStream = new BufferedOutputStream(bundle.createEntry(wizardFileState.getFileName(), "text/xml"));

		outputStream.write(reportSpecification.getBytes(Charset.forName("UTF-8")));

		outputStream.flush();
		outputStream.close();

		return wizardFileState.getFileName();

	}

	@Override
	public int getProcessingOrder() {
		return 100001;
	}

}
