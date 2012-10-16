package org.saiku.reporting.core.builder;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class TestDataTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object data[][] = new Object[][] {
		{"LibBase","xy",121745},
		{"LibBase","xy",122900},
		{"LibBase","xz",25689},
		{"LibRepository","xy",63655},
		{"LibRepository","xz",72896},
		{"LibRepository","xz",368263},
		{"LibRepository","xz",248320},
		{"LibDocBundle","xy",71186},
		{"LibDocBundle","xy",69464},
		{"LibDocBundle","xz",3375047},
		{"LibDocBundle","xz",92335}
	};
	Object columnNames[] = new Object[] {"Column1", "Column2", "Column3"};

	public Vector<Vector<Object>> generateData() {
		Vector<Vector<Object>> v = new Vector<Vector<Object>>();
		for (int i = 0; i < data.length; i++) {
			Vector<Object> r = new Vector<Object>();
			for (int j = 0; j < data[i].length; j++) {
				r.add(data[i][j]);
			}
			v.add(r);
		}
		return v;
	}
	
	public Vector<Object> getColumnNames() {
		Vector<Object> names = new Vector<Object>();
		for (int i = 0; i < columnNames.length; i++) {
			names.add(columnNames[i]);
		}
		return names;
	}
	
	public TestDataTableModel() {
		super();
		setDataVector(data, columnNames);
	}
	
	public TestDataTableModel(boolean libsOnly) {
		super();
		if (libsOnly) {
			Vector<Vector<Object>> vData = generateData();
			vData.remove(vData.size() - 1);
			vData.remove(vData.size() - 1);
			setDataVector(vData, getColumnNames());
		} else {
			setDataVector(data, columnNames);
		}
	}
	
	
}
