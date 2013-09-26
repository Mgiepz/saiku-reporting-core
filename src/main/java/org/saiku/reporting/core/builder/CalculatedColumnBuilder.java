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
