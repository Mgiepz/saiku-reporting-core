package org.saiku.reporting.core.builder;


public class TestDataFactory {	
	public TestDataTableModel getAllData() {
		return new TestDataTableModel();
	}
	
	public TestDataTableModel getLibData(boolean libOnly) {
		return new TestDataTableModel(libOnly);
	}
}
