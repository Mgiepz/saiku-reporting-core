package org.saiku.reporting.core.writer;

import java.io.IOException;

import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterState;
import org.pentaho.reporting.libraries.docbundle.WriteableDocumentBundle;

public class SaikuReportSpecificationWriteHandler implements
		BundleWriterHandler {

	@Override
	public String writeReport(WriteableDocumentBundle bundle,
			BundleWriterState state) throws IOException, BundleWriterException {
		
		
		
		return null;
	}

	@Override
	public int getProcessingOrder() {
		return 100001;
	}

}
