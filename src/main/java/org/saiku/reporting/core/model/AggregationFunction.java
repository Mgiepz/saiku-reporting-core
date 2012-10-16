package org.saiku.reporting.core.model;

public enum AggregationFunction implements AggregationClassMapper {
	
	NONE(null),
	SUM("org.pentaho.reporting.engine.classic.core.function.ItemSumFunction"),
	AVERAGE("org.pentaho.reporting.engine.classic.core.function.ItemAvgFunction"),
	COUNT("org.pentaho.reporting.engine.classic.core.function.ItemCountFunction"),
	COUNT_DISTINCT("org.pentaho.reporting.engine.classic.core.function.CountDistinctFunction"),
	MINIMUM("org.pentaho.reporting.engine.classic.core.function.ItemMinFunction"),
	MAXIMUM("org.pentaho.reporting.engine.classic.core.function.ItemMaxFunction");

	private String className;

	private AggregationFunction(String className) {
		this.className = className;
	}

	@Override
	public java.lang.String getClassName() {
		return this.className;
	}

	@Override
	public Class<? extends Object> getClassObject(){
		
		if (className==null) return null;
		
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
