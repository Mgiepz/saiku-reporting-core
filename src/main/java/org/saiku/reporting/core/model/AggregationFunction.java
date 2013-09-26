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
package org.saiku.reporting.core.model;

public enum AggregationFunction implements AggregationClassMapper {
	
	NONE(null),
	SUM("org.pentaho.reporting.engine.classic.core.function.ItemSumFunction"),
	GROUPSUM("org.pentaho.reporting.engine.classic.core.function.TotalGroupSumFunction"),
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
