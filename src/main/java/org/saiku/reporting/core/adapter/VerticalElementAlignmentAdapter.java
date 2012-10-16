package org.saiku.reporting.core.adapter;

import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportParserUtil;

public class VerticalElementAlignmentAdapter extends ElementAlignmentAdapter {
	
	public ElementAlignment unmarshal(String s) throws Exception {	
		return ReportParserUtil.parseVerticalElementAlignment(s.toLowerCase(), null);
	}

}
