package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jtableStuff.JTableData;
import jtableStuff.MyTableDataPanel;
import models.DB4oConnectionInfo;
import models.DB4oModel;
import staticClasses.MyConnections;
import utilities.DB4oConnection;

public class MyDB4oTabbedView extends AbstractMyDB4oTabbedView implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tabName = null;
	private Class<DB4oModel> db4oClass = null;
	private MyTableDataPanel centerPanel = null;
	private JButton truncateButton;
	private JButton deleteSelectedButton;
	private JButton addRecordButton;
	private AddNewRecordView anrv;

	public MyDB4oTabbedView(Class<DB4oModel> className) {

		this.setTabName(className.getSimpleName());
		this.setDb4oClass(className);
		this.setLayout(new BorderLayout());

	}

	public void addStandardNorthPanel() {

		JPanel northPanel = new JPanel();
		this.add(northPanel, BorderLayout.NORTH);

		
		addRecordButton = new JButton("Add Record");

		addRecordButton.addActionListener(this);
		
		Boolean addingIsAllowable = false;
		
		DB4oModel db4m = null;
		try {
			 db4m =  this.getDb4oClass().newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			
		Method m = null;
		
			try {
				m = db4m.getClass().getMethod("isCanAddNewRecordInObjectViewer", null);
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			try {
				addingIsAllowable = (Boolean) m.invoke(db4m, null);
				if(addingIsAllowable){
					addRecordButton.setEnabled(true);
				}
				else{
					addRecordButton.setEnabled(false);
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
	
		
		truncateButton = new JButton("Truncate");

		truncateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("truncating...");

				if (db4oClass != null) {

					String[] choices = { "Yes", "Cancel" };

					int choice = JOptionPane.showOptionDialog(null,
							"Are you sure you wish to truncate all data for class = "
									+ db4oClass.getSimpleName() + "?",
							"Truncate?", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, choices,
							"Cancel");

					if (choice > 0) {
						return;
					}

					DB4oConnectionInfo dbci = DB4oModel.hcd.get(db4oClass);

					if (dbci == null) {
						System.out.println("!!!!null connection info!!!!");
						return;
					}

					DB4oConnection dbc = MyConnections.getConnection(dbci);

					if (dbc != null) {
						dbc.deleteFromDatabaseByClass(db4oClass);
						dbc.commit();
						refreshView(db4oClass);
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"No class was found for this view.");
					return;
				}

			}

		});

		deleteSelectedButton = new JButton("Delete Selected");

		deleteSelectedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int[] selectedRows = centerPanel.getMyTable().getSelectedRows();
				
				Vector<DB4oModel> v = new Vector<DB4oModel>();
				
				int lastColumn = centerPanel.getMyTable().getColumnCount() -1;
				
				boolean triedToDeleteANonDB4oModel = false;
				
				for(int i: selectedRows){
					
					DB4oModel o;
					try {
						o = (DB4oModel) centerPanel.getMyTable().getValueAt(i, lastColumn);
						v.add(o);
					} catch (Exception e) {
						triedToDeleteANonDB4oModel = true;
						e.printStackTrace();
						continue;
					}
					
				}
				
				for(DB4oModel dm: v){
					
					dm.delete();
				}
				
				
				refreshView(db4oClass);
				
				if(triedToDeleteANonDB4oModel){
					JOptionPane.showMessageDialog(null, "Tried to delete a non-DB4oModel, delete request was ignored");
				}

			}

		});

		northPanel.add(addRecordButton);
		northPanel.add(deleteSelectedButton);
		northPanel.add(truncateButton);

		this.revalidate();
		this.repaint();
	}

	public void refreshView(Class<DB4oModel> c) {

		addDataPaneWithClass(c);

	}

	public void addDataPaneWithModel(DB4oModel dm) {

		if (centerPanel != null) {
			this.remove(centerPanel);
		}

		JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
		// JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
		centerPanel = new MyTableDataPanel(jta);

		this.add(centerPanel, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();
	}

	// public void addDataPaneWithClass(Class<?> dm){
	//
	// this.removeAll();
	//
	// JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
	// // JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
	// MyTableDataPanel mtdp = new MyTableDataPanel(jta);
	//
	// this.add(mtdp,BorderLayout.CENTER);
	//
	// this.revalidate();
	// this.repaint();
	// }

	public void addDataPaneWithClass(Class<DB4oModel> c) {

		if (centerPanel != null) {
			this.remove(centerPanel);
		}

		db4oClass = (Class<DB4oModel>) c;

		DB4oConnection dbc = DB4oObjectViewer.dbch.get(c.getName());
//		JTableData jta = dbc.getTableDataForClass(c);
		JTableData jta = dbc.getTableDataForClassAndSuperClasses(c);
		// JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
		centerPanel = new MyTableDataPanel(jta);

		this.add(centerPanel, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();
	}

	public void addDataPanel(MyTableDataPanel mtdp) {

		if (centerPanel != null) {
			this.remove(centerPanel);
		}

		centerPanel = mtdp;
		this.add(centerPanel, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();

	}

	public void addTabComponent(final MyDB4oTabbedView mdtv,
			final MyTabbedPane mtp) {

		final int index = mtp.getJtp().indexOfComponent(this);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel();
		JLabel titleLabel = new JLabel(this.getTabName());
		centerPanel.add(titleLabel);
		p.add(centerPanel, BorderLayout.CENTER);
		JPanel eastPanel = new JPanel();
		p.add(eastPanel, BorderLayout.EAST);
		JButton jbtn = new JButton("x");
		jbtn.setPreferredSize(new Dimension(15, 15));
		eastPanel.add(jbtn);
		jbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				mtp.removeTab(mdtv);
				MyTabbedPane.hclass.remove(mdtv.getDb4oClass());

			}

		});
		mtp.getJtp().setTabComponentAt(index, p);

	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public Class<DB4oModel> getDb4oClass() {
		return db4oClass;
	}

	public void setDb4oClass(Class<DB4oModel> db4oClass) {
		this.db4oClass = db4oClass;
	}

	public MyTableDataPanel getCenterPanel() {
		return centerPanel;
	}

	public void setCenterPanel(MyTableDataPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	public AddNewRecordView getAnrv() {
		return anrv;
	}

	public void setAnrv(AddNewRecordView anrv) {
		this.anrv = anrv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		

				System.out.println("add record...");

				if (db4oClass != null) {


					DB4oConnectionInfo dbci = DB4oModel.hcd.get(db4oClass);

					if (dbci == null) {
						System.out.println("!!!!null connection info!!!!");
						return;
					}

					if(anrv != null){
						anrv.dispose();
					}
					anrv = new AddNewRecordView(this);
					anrv.setVisible(true);
						//refreshView(db4oClass);
					
				} else {
					JOptionPane.showMessageDialog(null,
							"No class was found for this view.");
					return;
				}
		
	}

}
