package jtableStuff;

public class JTableData {
	
	private Object[] columnNames = null;
	private Object[][] data = null;
	private Object objectForWhichDataWasRetrieved = null;
	
	
	public JTableData(Object o, Object[] columnNames, Object[][] data) {

		this.setObjectForWhichDataWasRetrieved(o);
		this.setColumnNames(columnNames);
		this.setData(data);
	}



	public Object[] getColumnNames() {
		return columnNames;
	}



	public void setColumnNames(Object[] columnNames) {
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



}
