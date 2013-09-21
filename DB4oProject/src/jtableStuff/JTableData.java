package jtableStuff;

import java.util.Vector;

public class JTableData {
	
	private String[] columnNames = null;
	private Object[][] data = null;
	private Vector<Class<?>> columnTypes = null;
	private Object objectForWhichDataWasRetrieved = null;
	
	
	public JTableData(Object o, Vector<Class<?>> columnTypes, String[] columnNames, Object[][] data) {

		this.setObjectForWhichDataWasRetrieved(o);
		this.setColumnNames(columnNames);
		this.setData(data);
		this.setColumnTypes(columnTypes);
	}



	public String[] getColumnNames() {
		return columnNames;
	}



	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}



	public Object[][] getData() {
		return data;
	}



	public void setData(Object[][] data) {
		this.data = data;
	}



	public Object getObjectForWhichDataWasRetrieved() {
		return objectForWhichDataWasRetrieved;
	}



	public void setObjectForWhichDataWasRetrieved(
			Object objectForWhichDataWasRetrieved) {
		this.objectForWhichDataWasRetrieved = objectForWhichDataWasRetrieved;
	}



	public Vector<Class<?>> getColumnTypes() {
		return columnTypes;
	}



	public void setColumnTypes(Vector<Class<?>> columnTypes) {
		this.columnTypes = columnTypes;
	}



}
