package org.saiku.reporting.core.builder;

import org.pentaho.reporting.engine.classic.core.parameters.DefaultParameterDefinition;
import org.pentaho.reporting.engine.classic.core.parameters.ReportParameterDefinition;
import org.saiku.reporting.core.model.ReportSpecification;

public class ParameterDefinitionBuilder {
	
	public ReportParameterDefinition build(ReportSpecification reportSpec) {

		DefaultParameterDefinition paramDef = new DefaultParameterDefinition();

//		List<SaikuParameter> parameters = model.getParameters();
//		reportSpec.get
//
//		//Generate Filter Parameters
//
//		for (SaikuParameter saikuParameter : parameters) {
//
//			final String categoryId = saikuParameter.getCategory();
//			final String columnId = saikuParameter.getId();	
//			final String parameterName = "F_" + categoryId + "_" + columnId;
//
//			if(saikuParameter.getType().equals("String")){
//				final DefaultListParameter listParam = 
//					new DefaultListParameter(categoryId + "." + columnId, columnId, columnId, 
//							parameterName, true, false, String[].class);
//				listParam.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.TYPE,
//						ParameterAttributeNames.Core.TYPE_LIST);
//				listParam.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.LABEL,
//						saikuParameter.getName());
//				paramDef.addParameterDefinition(listParam);
//
//			}
//			if(saikuParameter.getType().equals("Date")){
//				String nameFrom = parameterName + "_FROM";
//				String nameTo = parameterName + "_TO";
//
//				final PlainParameter plainParameterFrom = new PlainParameter(nameFrom, java.util.Date.class);
//				plainParameterFrom.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.LABEL,
//						saikuParameter.getName() + " from");
//				plainParameterFrom.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.TYPE,
//						ParameterAttributeNames.Core.TYPE_DATEPICKER);
//				plainParameterFrom.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.DEFAULT_VALUE_FORMULA,
//				"TODAY()");				
//				paramDef.addParameterDefinition(plainParameterFrom);
//
//				final PlainParameter plainParameterTo = new PlainParameter(nameTo, java.util.Date.class);
//				plainParameterTo.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.LABEL,
//						saikuParameter.getName() + " until");
//				plainParameterTo.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.TYPE,
//						ParameterAttributeNames.Core.TYPE_DATEPICKER);
//				plainParameterTo.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.DEFAULT_VALUE_FORMULA,
//				"TODAY()");	
//				paramDef.addParameterDefinition(plainParameterTo);
//
//			}
//			
//			if(saikuParameter.getType().equals("Numeric")){
//				final DefaultListParameter listParam = 
//					new DefaultListParameter(categoryId + "." + columnId, columnId, columnId, 
//							parameterName, true, false, Object[].class);
//				listParam.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.TYPE,
//						ParameterAttributeNames.Core.TYPE_LIST);
//				listParam.setParameterAttribute(
//						ParameterAttributeNames.Core.NAMESPACE, 
//						ParameterAttributeNames.Core.LABEL,
//						saikuParameter.getName());
//				paramDef.addParameterDefinition(listParam);
//
//			}
//
//			
//		}
//
//		//set layout horizontal
//		paramDef.setAttribute(
//				ParameterAttributeNames.Core.NAMESPACE, 
//				ParameterAttributeNames.Core.LAYOUT,
//				ParameterAttributeNames.Core.LAYOUT_HORIZONTAL
//		);
//
		return paramDef;

	}

}
