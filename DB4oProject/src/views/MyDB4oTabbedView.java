package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import jtableStuff.JTableData;
import jtableStuff.MyTableDataPanel;
import models.DB4oConnectionInfo;
import models.DB4oModel;
import staticClasses.MyConnections;
import utilities.DB4oConnection;
import utilities.DB4oInternalId;

public class MyDB4oTabbedView extends AbstractMyDB4oTabbedView implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tabName = null;
	private Class<DB4oModel> db4oClass = null;
	private MyTableDataPanel centerDataPanel = null;
	private JButton truncateButton;
	private JButton deleteSelectedButton;
	private JButton addRecordButton;
	private AddNewRecordView anrv;
	private JButton editRecordButton;
	private JButton refreshButton;
	private EditSingleRecordView erv;
	private EditMultipleRecordsView emrv;
	private JButton viewObjectGraph;
	private JButton filterDataButton;
	private JButton createDataViewButton;

	public MyDB4oTabbedView(Class<DB4oModel> className) {

		this.setTabName(className.getSimpleName());
		this.setDb4oClass(className);
		this.setLayout(new BorderLayout());

	}

	public void addStandardNorthPanel() {

		JPanel northPanel = new JPanel();
		this.add(northPanel, BorderLayout.NORTH);

		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(this);

		addRecordButton = new JButton("Add Record");
		addRecordButton.addActionListener(this);

		Boolean addingIsAllowable = false;

		DB4oModel db4m = null;
		try {
			db4m = this.getDb4oClass().newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Method m = null;

		try {
			m = db4m.getClass().getMethod("isCanAddNewRecordInObjectViewer",
					null);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			addingIsAllowable = (Boolean) m.invoke(db4m, null);
			if (addingIsAllowable) {
				addRecordButton.setEnabled(true);
			} else {
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

		editRecordButton = new JButton("Edit Record(s)");
		editRecordButton.addActionListener(this);

		Boolean editingIsAllowable = false;

		DB4oModel db4m1 = null;
		try {
			db4m1 = this.getDb4oClass().newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Method m1 = null;

		try {
			m1 = db4m.getClass().getMethod("isCanEditRecordInObjectViewer",
					null);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			editingIsAllowable = (Boolean) m.invoke(db4m, null);
			if (editingIsAllowable) {
				editRecordButton.setEnabled(true);
			} else {
				editRecordButton.setEnabled(false);
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

		deleteSelectedButton = new JButton("Delete Record(s)");

		deleteSelectedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int[] selectedRows = centerDataPanel.getMyTable()
						.getSelectedRows();

				if (selectedRows.length < 1) {
					JOptionPane.showMessageDialog(null,
							"No row selected to delete.");
					return;
				}

				if (selectedRows.length > 1) {
					
					String[] choices = { "Yes", "Cancel" };

					int choice = JOptionPane.showOptionDialog(null, "Note: You have selected multiple records to delete.\nAre you sure you wish to delete?",
							"Delete MULTIPLE records?", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, choices, "Cancel");

					if (choice > 0) {	
						return;
					}
				}

				Vector<DB4oModel> v = new Vector<DB4oModel>();

				int lastColumn = centerDataPanel.getMyTable().getColumnCount() - 1;

				boolean triedToDeleteANonDB4oModel = false;

				for (int i : selectedRows) {

					DB4oModel o;
					try {
						o = (DB4oModel) centerDataPanel.getMyTable()
								.getValueAt(i, lastColumn);
						v.add(o);
					} catch (Exception e) {
						triedToDeleteANonDB4oModel = true;
						e.printStackTrace();
						continue;
					}

				}

				for (DB4oModel dm : v) {

					dm.delete();
				}

				refreshView(db4oClass);

				if (triedToDeleteANonDB4oModel) {
					JOptionPane
							.showMessageDialog(null,
									"Tried to delete a non-DB4oModel, delete request was ignored");
				}

			}

		});

		northPanel.add(refreshButton);
		northPanel.add(addRecordButton);
		northPanel.add(editRecordButton);
		northPanel.add(deleteSelectedButton);

		viewObjectGraph = new JButton("View Object Graph");
		viewObjectGraph.addActionListener(this);

		filterDataButton = new JButton("Filter");
		filterDataButton.addActionListener(this);

		// here we create a view that can be saved by the user, the view can be
		// used to delete records
		// we should keep the main view as ALWAYS having ALL the data
		createDataViewButton = new JButton("Create View");
		createDataViewButton.addActionListener(this);

		northPanel.add(viewObjectGraph);
		northPanel.add(filterDataButton);
		northPanel.add(createDataViewButton);

		northPanel.add(truncateButton);

		this.revalidate();
		this.repaint();
	}

	public void refreshView(Class<DB4oModel> c) {

		addDataPaneWithClass(c);

	}

	public void addDataPaneWithModel(DB4oModel dm) {

		if (centerDataPanel != null) {
			this.remove(centerDataPanel);
		}

		JTableData jta = dm.getDb4oConn().getTableDataForObjectsClass(dm);
		// JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
		centerDataPanel = new MyTableDataPanel(jta);

		this.add(centerDataPanel, BorderLayout.CENTER);

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

		if (centerDataPanel != null) {
			this.remove(centerDataPanel);
		}

		db4oClass = (Class<DB4oModel>) c;

		DB4oConnection dbc = DB4oObjectViewer.dbch.get(c.getName());
		// JTableData jta = dbc.getTableDataForClass(c);
		JTableData jta = null;
		try {
			jta = dbc.getTableDataForClassAndSuperClasses(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// JTableData jta = dm.getDb4oConn().getTableDataForObject(dm);
		centerDataPanel = new MyTableDataPanel(jta);

		this.add(centerDataPanel, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();
	}

	public void addDataPanel(MyTableDataPanel mtdp) {

		if (centerDataPanel != null) {
			this.remove(centerDataPanel);
		}

		centerDataPanel = mtdp;
		this.add(centerDataPanel, BorderLayout.CENTER);

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
		return centerDataPanel;
	}

	public void setCenterPanel(MyTableDataPanel centerPanel) {
		this.centerDataPanel = centerPanel;
	}

	public AddNewRecordView getAnrv() {
		return anrv;
	}

	public void setAnrv(AddNewRecordView anrv) {
		this.anrv = anrv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == refreshButton) {

			System.out.println("refreshing");
			refreshView(db4oClass);
			System.out.println("refreshed");
		}

		else if (e.getSource() == addRecordButton) {

			System.out.println("add record...");

			if (db4oClass != null) {

				if (anrv != null) {
					anrv.dispose();
				}
				anrv = new AddNewRecordView(this);
				anrv.setVisible(true);
				// refreshView(db4oClass);

			} else {
				JOptionPane.showMessageDialog(null,
						"No class was found for this view.");
				return;
			}
		} else if (e.getSource() == editRecordButton) {

			System.out.println("editing record(s)...");

			boolean oneRecordOnly = true;

			int[] rows = centerDataPanel.getMyTable().getSelectedRows();

			if (rows.length < 1) {
				JOptionPane.showMessageDialog(null, "No row selected to edit.");
				return;
			}
			if (rows.length > 1) {
				oneRecordOnly = false;
				JOptionPane
						.showMessageDialog(null,
								"Note: You are editing multiple objects at once. Be extra careful.");
				// return;
			}

			// Enumeration<TableColumn> cols =
			// centerDataPanel.getMyTable().getColumnModel().getColumns();

			int cols = centerDataPanel.getMyTable().getColumnCount();
			TableModel tm = centerDataPanel.getMyTable().getModel();

			Integer colIndexForDB4oInternalId = null;

			for (int i = 0; i < cols; i++) {
				String columnName = tm.getColumnName(i);
				if (columnName.equalsIgnoreCase("db4oInternalId")) {
					colIndexForDB4oInternalId = i;
					break;
				}
			}

			if (colIndexForDB4oInternalId == null) {

				JOptionPane.showMessageDialog(null,
						"No column with DB4oInternalId was found.");
				return;

			}

			if (oneRecordOnly) {
				processEditActionForOneRecordOnly(rows[0],
						colIndexForDB4oInternalId);
			} else {
				processEditActionForMultipleRecords(rows,
						colIndexForDB4oInternalId);
			}

		}

	}

	private void processEditActionForMultipleRecords(int[] rows,
			Integer colIndexForDB4oInternalId) {

		DB4oConnectionInfo dbci = null;
		DB4oConnection dbc = null;

		Vector<DB4oModel> db4oModelObjectVector = new Vector<DB4oModel>();

		for (int row : rows) {
			DB4oInternalId dii = (DB4oInternalId) centerDataPanel.getMyTable()
					.getValueAt(row, colIndexForDB4oInternalId);

			if (dii == null) {
				System.out.println("null internal id !!!");
				return;
			}

			if (dbci == null) {
				dbci = DB4oModel.hcd.get(db4oClass);
			}
			if (dbc == null) {
				dbc = MyConnections.getConnection(dbci);
			}

			DB4oModel db4m = dbc.getDb().ext().getByID(dii.getId());
			dbc.getDb().ext().activate(db4m);

			db4oModelObjectVector.add(db4m);
		}

		if (emrv != null) {
			emrv.dispose();
		}
		emrv = new EditMultipleRecordsView(db4oModelObjectVector, this);
		emrv.setVisible(true);

	}

	private void processEditActionForOneRecordOnly(int row,
			Integer colIndexForDB4oInternalId) {

		DB4oInternalId dii = (DB4oInternalId) centerDataPanel.getMyTable()
				.getValueAt(row, colIndexForDB4oInternalId);

		// DB4oInternalId dii = (DB4oInternalId)
		// centerDataPanel.getMyTable().getValueAt(row, 4);

		if (dii == null) {
			System.out.println("null internal id !!!");
			return;
		}

		DB4oConnectionInfo dbci = DB4oModel.hcd.get(db4oClass);

		if (dbci == null) {
			System.out.println("null connection info !!!");
			return;
		}

		DB4oConnection dbc = MyConnections.getConnection(dbci);

		if (dbc == null) {
			System.out.println("null connection !!!");
			return;
		}

		DB4oModel db4m = dbc.getDb().ext().getByID(dii.getId());
		dbc.getDb().ext().activate(db4m);

		if (erv != null) {
			erv.dispose();
		}
		erv = new EditSingleRecordView(db4m, this);
		erv.setVisible(true);
	}

	public JButton getRefreshButton() {
		return refreshButton;
	}

	public void setRefreshButton(JButton refreshButton) {
		this.refreshButton = refreshButton;
	}

	public EditMultipleRecordsView getEmrv() {
		return emrv;
	}

	public void setEmrv(EditMultipleRecordsView emrv) {
		this.emrv = emrv;
	}

}
