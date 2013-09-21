package jtableStuff;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import utilities.DB4oInternalId;

public class MyTableDataPanel extends JPanel {
	
	JTable myTable = null;
	JScrollPane scrollPane = null;

	public MyTableDataPanel(JTableData jta) {
	
			setBackground(Color.white);
			
			this.setLayout(new BorderLayout());
		
			myTable = new JTable(jta.getData(),jta.getColumnNames()){
				
			
				public boolean isCellEditable(int row, int col){
				
					Object o = this.getValueAt(row, col);
					
					if(o instanceof DB4oInternalId){
						return false;
					}
					
					if(o instanceof String || o instanceof Integer || o instanceof Long){
						return true;
					}
					return false; 
					
				}
				
			};
			myTable.setDefaultRenderer(Object.class, new MyTableCellRenderer());
			myTable.setRowSelectionAllowed(true);	
			myTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			
			// Add the table to a scrolling pane
			scrollPane = new JScrollPane(myTable);
			this.add(scrollPane, BorderLayout.CENTER);
			
		
	}
	
	public JTable getMyTable() {
		return myTable;
	}

	public void setMyTable(JTable myTable) {
		this.myTable = myTable;
	}



}
