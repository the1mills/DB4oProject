package jtableStuff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import models.DB4oModel;
import utilities.DB4oInternalId;
import views.MyDefaultMutableTreeNode;

import com.db4o.ext.Db4oUUID;

public class MyTableDataPanel extends JPanel {
	
	private JTable myTable = null;
	private JTableData jta = null;
	private JScrollPane scrollPane = null;
	private DefaultTableModel dftm;
	private MouseAdapter ml;
	private JPopupMenu tableDetailsPopup = null;
	
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
			
			//myTable.getTableHeader().setPreferredSize(new Dimension(100,35));
//			myTable.getTableHeader().setDefaultRenderer(new TableCellRenderer(){
//
//				@Override
//				public Component getTableCellRendererComponent(JTable arg0,
//						Object arg1, boolean arg2, boolean arg3, int arg4,
//						int arg5) {
//					JPanel p = new JPanel();
//					p.setPreferredSize(new Dimension(1000,40));
//					p.setBackground(Color.white);
//					JLabel jl = new JLabel(arg1.toString());
//					jl.setForeground(Color.BLUE);
//					p.add(jl);
//					return p;
//				}
//				
//			});
			
			

			ml = new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					
					boolean isRightClick = SwingUtilities.isRightMouseButton(e);
					
					if(isRightClick){
						if(tableDetailsPopup != null){
							tableDetailsPopup.setVisible(false);
						}
						createPopUpMenuForTable(e.getX(),e.getY());
					}
				}
			};
			myTable.addMouseListener(ml);
			
			
			myTable.getTableHeader().setReorderingAllowed(false);
//			myTable.getTableHeader().setBackground(Color.DARK_GRAY);
//			myTable.getTableHeader().setForeground(Color.pink);
			myTable.setDefaultRenderer(Object.class, new MyTableCellRenderer());
			myTable.setRowSelectionAllowed(true);	
			myTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			scrollPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.addMouseListener(ml);
			myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.add(scrollPane, BorderLayout.CENTER);
			
		
	}
	
	private void createPopUpMenuForTable(int x, int y) {

			JPopupMenu popup = new JPopupMenu();

				JMenuItem item1 = new JMenuItem("Get Count");
				JMenuItem item2 = new JMenuItem("Open Table View");
				JMenuItem item3 = new JMenuItem("Open Object Graph View");
				JMenuItem item4 = new JMenuItem("Open Data Controls");
				JMenuItem item5 = new JMenuItem("Truncate Data");

				popup.add(item1);
				popup.add(item2);
				popup.add(item3);
				popup.add(item4);
				popup.add(item5);

				
				item1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						int count = getTableRowCount();
						JOptionPane.showMessageDialog(null, "Row count: " + count);
					}

				});

				
				item2.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

					}

				});

				item3.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

					}

				});

				item4.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

					}

				});
				
				tableDetailsPopup = popup;
				popup.show(myTable, x, y);
		
	}

	protected int getTableRowCount() {
		// TODO Auto-generated method stub
		return jta.getData().length;
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

	public JPopupMenu getTableDetailsPopup() {
		return tableDetailsPopup;
	}

	public void setTableDetailsPopup(JPopupMenu tableDetailsPopup) {
		this.tableDetailsPopup = tableDetailsPopup;
	}



}
