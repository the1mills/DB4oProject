package jtableStuff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import models.DB4oModel;
import staticClasses.MyConnections;
import utilities.DB4oConnection;

public class MakeJTableFrame extends JFrame implements ActionListener{

	private JPanel topPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton deleteButton;
	private JButton viewButton;
	private JTableData jta;


	public MakeJTableFrame(JTableData jta) {
		
		this.setJta(jta);
		
		setTitle("Simple Table Application");
		setSize(600, 200);
		setBackground(Color.white);

		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		setContentPane(topPanel);

		table = new JTable(jta.getData(),jta.getColumnNames());
		table.setRowSelectionAllowed(true);	
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		// Add the table to a scrolling pane
		scrollPane = new JScrollPane(table);
		topPanel.add(scrollPane, BorderLayout.CENTER);
		
		deleteButton = new JButton("Delete");
		viewButton = new JButton("View Object");
		JPanel northPanel = new JPanel();
		topPanel.add(northPanel,BorderLayout.NORTH);
		northPanel.add(deleteButton);
		northPanel.add(viewButton);
		deleteButton.addActionListener(this);
		viewButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource() == deleteButton){
		
		int[] selectedRows = table.getSelectedRows();
		
		if(selectedRows.length < 1){
			JOptionPane.showMessageDialog(null, "No row selected to delete.");
			return;
		}
		
		Object o = null;
		
		for(Integer i: selectedRows){
			
			o =  table.getModel().getValueAt(i, 1);
			if(o instanceof DB4oModel){
				((DB4oModel) o).delete(false);
			}
				
		}
		
		((DB4oModel) o).commit();
		
		 
		  
//		DB4oConnection dbc =  MyConnections.getConnection("rudolf");
//		JTableData jta = dbc.getThreeColumnListOfAllDBObjects();
//		redrawJTable(jta);
		  
		}
		else if(arg0.getSource() == viewButton){
			
			int[] selectedRows = table.getSelectedRows();
			
			if(selectedRows.length < 1){
				JOptionPane.showMessageDialog(null, "No row selected to view.");
				return;
			}
			
			if(selectedRows.length > 1){
				JOptionPane.showMessageDialog(null, "More than one row selected to view.");
				return;
			}
			
			Integer i = selectedRows[0];
			
			Object o = table.getModel().getValueAt(i, 1);
			
//			DB4oConnection dbc =  MyConnections.getConnection("rudolf");
//			 JTableData jta = dbc.getTableDataForObject(o);
//			 dbc.openJTableWithFrame(jta);
			 
			 return;
	
		}
	}
	

	private void redrawJTable(JTableData tableData) {
		topPanel.remove(scrollPane);
		table = new JTable(tableData.getData(),tableData.getColumnNames());
		table.setRowSelectionAllowed(true);	
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scrollPane = new JScrollPane(table);
		topPanel.add(scrollPane, BorderLayout.CENTER);
		topPanel.revalidate();
		topPanel.repaint();
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(JButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public JTableData getJta() {
		return jta;
	}

	public void setJta(JTableData jta) {
		this.jta = jta;
	}

}