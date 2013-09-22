package jtableStuff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import models.DB4oModel;

import utilities.DB4oInternalId;

import com.db4o.ext.Db4oUUID;

public class MyTableDataPanel extends JPanel {
	
	private JTable myTable = null;
	private JTableData jta = null;
	private JScrollPane scrollPane = null;
	private DefaultTableModel dftm;

	public MyTableDataPanel(JTableData jta) {
	
			this.setJta(jta);
			setBackground(Color.white);
			
			this.setLayout(new BorderLayout());
			
			 dftm = new DefaultTableModel(jta.getData(),jta.getColumnNames()){
				
				
				  @Override  
			      public Class<?> getColumnClass(int col) {  
					  return getJta().getColumnTypes().get(col);
			    }  
			  
			    @Override  
			    public boolean isCellEditable(int row, int col){
					
					//we have to check for column type, not cell value, because if cell value is null, we will 
					//accidentally be able to edit that cell.
				
//					Class<?> c = myTable.getModel().getColumnClass(col);
//					
//					if(c.equals(Db4oUUID.class)){
//						return false;
//					}
//					
//					if(c.equals(DB4oInternalId.class)){
//						return false;
//					}
//					
//					Object o = this.getValueAt(row, col);
//					
//					if(o instanceof String || o instanceof Integer || o instanceof Long){
//						return true;
//					}
					return false; 
					
				}
			    
			
			};
		
			myTable = new JTable(dftm){
				
			};
			
			
			Enumeration<TableColumn> cols =myTable.getColumnModel().getColumns();
			
			while(cols.hasMoreElements()){
				
				TableColumn tc = cols.nextElement();
				int colIndex = tc.getModelIndex();
			
				Class<?> c = myTable.getModel().getColumnClass(colIndex);
				
				if(c.equals(Db4oUUID.class)){
					tc.setPreferredWidth(100);
					continue;
				}
				else if(c.equals(String.class)){
					tc.setPreferredWidth(200);
					continue;
				}
				else if(c.equals(DB4oModel.class)){
					tc.setPreferredWidth(300);
					continue;
				}
				else if(c.equals(DB4oInternalId.class)){
					tc.setPreferredWidth(100);
					continue;
				}
				else if(c.equals(Integer.class)){
					tc.setPreferredWidth(100);
					continue;
				}
				else if(c.equals(Long.class)){
					tc.setPreferredWidth(100);
					continue;
				}
				else if(c.equals(Date.class)){
					tc.setPreferredWidth(100);
					continue;
				}
				else if(c.equals(Boolean.class)){
					tc.setPreferredWidth(75);
					continue;
				}
				tc.setPreferredWidth(200);
			}
			
		
			myTable.setDefaultRenderer(Object.class, new MyTableCellRenderer());
			myTable.setRowSelectionAllowed(true);	
			myTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			scrollPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.add(scrollPane, BorderLayout.CENTER);
			
		
	}
	
	public JTable getMyTable() {
		return myTable;
	}

	public void setMyTable(JTable myTable) {
		this.myTable = myTable;
	}

	public JTableData getJta() {
		return jta;
	}

	public void setJta(JTableData jta) {
		this.jta = jta;
	}



}
