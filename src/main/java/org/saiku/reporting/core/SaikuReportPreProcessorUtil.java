package org.saiku.reporting.core;

import java.util.ArrayList;

import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.saiku.reporting.core.model.GroupDefinition;
import org.saiku.reporting.core.model.ReportSpecification;
import org.saiku.reporting.core.model.types.GroupType;

public class SaikuReportPreProcessorUtil {

	public static ReportSpecification loadReportSpecification(MasterReport definition, ResourceManager resourceManager) {
		return null;

//		try {
//			final ResourceKey key = resourceManager.deriveKey(definition.getContentBase(), "your-json.file");
//
//		} catch (ResourceKeyCreationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		
//		final ResourceKey contentBase = definition.getContentBase();
//		final ResourceKey resourceKey = resourceManager.deriveKey(contentBase, String.valueOf(attribute));
//		final Resource resource = resourceManager.create(resourceKey, contentBase, WizardSpecification.class);
//		return (WizardSpecification) resource.getResource();
		
//		}
				

//try{
//	
//	 final ResourceKey butterflyKey = resourceManager.createKey(crosstab());
//	 
//		 
//}catch(Exception e){
//	
//}

		 //BundleUtilities.copyInto(definition, "/images/butterfly.wmf", butterflyKey, resourceManager);
		

//		return crosstab();
		
		
	}

//	private static ReportSpecification crosstab() {
//		ReportSpecification spec = new ReportSpecification();
//		
//		FieldDefinition column = new FieldDefinition();
//		column.setId("Column1");
//		ElementFormat headerFormat = new ElementFormat();
//		//headerFormat.se
//		column.setName("Test");
//		column.setHeaderFormat(headerFormat );
//
//		FieldDefinition column2 = new FieldDefinition();
//		column2.setId("Column2");
//
//		FieldDefinition column3 = new FieldDefinition();
//		column3.setId("Column3");
//
//		GroupDefinition colgroup = new GroupDefinition();
//		colgroup.setFieldId("Column1");
//		colgroup.setType(GroupType.CT_COLUMN);
//
////		GroupDefinition colgroup2 = new GroupDefinition();
////		colgroup2.setFieldId("Column1");
////		colgroup2.setType(GroupType.CT_COLUMN);
////		
////		GroupDefinition colgroup3 = new GroupDefinition();
////		colgroup3.setFieldId("Column1");
////		colgroup3.setType(GroupType.CT_COLUMN);
//		
//		GroupDefinition rowgroup = new GroupDefinition();
//		rowgroup.setFieldId("Column2");
//		rowgroup.setType(GroupType.CT_ROW);
//
//		spec.getTableDefinition().getGroups().add(colgroup);
////		spec.getTableDefinition().getGroupDefinitions().add(colgroup2);
////		spec.getTableDefinition().getGroupDefinitions().add(colgroup3);
////		
//		
//		spec.getTableDefinition().getGroups().add(rowgroup);
//
//		spec.getTableDefinition().getColumns().add(column);
////		spec.getTableDefinition().getColumns().add(column3);
////		spec.getTableDefinition().getColumns().add(column3);
////		spec.getTableDefinition().getColumns().add(column3);
//		
//		return spec;
//	}
	
//	private static ReportSpecification table() {
//		ReportSpecification spec = new ReportSpecification();
//		
//		Label label = new Label("test");
//
//		spec.getPageHeaders().add(label);
//		spec.getReportHeaders().add(label);
//
//		Label label2 = new Label("PEST");
//		spec.getReportHeaders().add(label2);
//		
//		FieldDefinition column = new FieldDefinition();
//		column.setId("Column1");
//		column.setName("Test");
//		column.setAggregationFunction(AggregationFunction.COUNT);
//		
//		FieldDefinition column2 = new FieldDefinition();
//		column2.setId("Column2");
//
////		FieldDefinition column3 = new FieldDefinition();
////		column3.setId("Column3");
////
////		FieldDefinition column4 = new FieldDefinition();
////		column3.setId("Column3");
////		
////		FieldDefinition column5 = new FieldDefinition();
////		column3.setId("Column3");
//		
//		GroupDefinition colgroup = new GroupDefinition();
//		colgroup.setFieldId("Column1");
//		colgroup.setDisplayName("Column1:");
//		colgroup.setType(GroupType.RELATIONAL);
//
//		spec.getGroupDefinitions().add(colgroup);
//		spec.getDetailsFooterBand().setVisible(false);
//		
//		
//		spec.getTableDefinition().getColumns().add(column);
//		spec.getTableDefinition().getColumns().add(column2);
////		spec.getTableDefinition().getColumns().add(column3);
////		spec.getTableDefinition().getColumns().add(column4);
////		spec.getTableDefinition().getColumns().add(column5);
//
//		return spec;
//	}

	public static boolean isCrosstab(ReportSpecification reportSpecification) {

		ArrayList<GroupDefinition> groupDefinitions = reportSpecification.getGroupDefinitions();
		
		for (GroupDefinition groupDefinition : groupDefinitions) {
			GroupType type = groupDefinition.getType();
			if(GroupType.CT_COLUMN.equals(type) || GroupType.CT_ROW.equals(type)){
				return true;
			}
		}
		return false;
	}
}
