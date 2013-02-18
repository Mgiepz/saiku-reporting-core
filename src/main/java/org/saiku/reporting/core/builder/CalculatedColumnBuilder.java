package org.saiku.reporting.core.builder;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.function.FormulaExpression;
import org.saiku.reporting.core.model.FieldDefinition;
import org.saiku.reporting.core.model.ReportSpecification;

public class CalculatedColumnBuilder{
	
	public void build(AbstractReportDefinition definition,
			ReportSpecification reportSpecification){

		for (FieldDefinition fieldDefinition : reportSpecification.getFieldDefinitions()) {
			if(fieldDefinition.getFormula()!=null){
				FormulaExpression calcColumn = new FormulaExpression();
				calcColumn.setName(fieldDefinition.getId());
				calcColumn.setFormula("=" + fieldDefinition.getFormula());
				definition.addExpression(calcColumn);
			}
		}
        
		
	}

}
