package org.saiku.reporting.core.adapter;

import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportParserUtil;

public class HorizontalElementAlignmentAdapter extends ElementAlignmentAdapter {
	
	public ElementAlignment unmarshal(String s) throws Exception {	
		return ReportParserUtil.parseHorizontalElementAlignment(s.toLowerCase(), null);
	}

}
