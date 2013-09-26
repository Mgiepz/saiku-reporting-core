/*******************************************************************************
 * Copyright 2013 Marius Giepz and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
