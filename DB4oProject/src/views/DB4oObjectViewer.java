package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import annotations.ViewableField;

import jtableStuff.JTableData;
import models.DB4oFile;
import models.DB4oModel;
import staticClasses.MyConnections;
import utilities.DB4oConnection;

public class DB4oObjectViewer extends JFrame implements ActionListener,
		WindowListener {

	// private MyHomeFolder mhf = null;
	public static MyHomeFolder folderOfDatabases;
	private MyTabbedPane mtp = new MyTabbedPane();
	private JPanel westpanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel southPanel = new JPanel();
	private JPanel northPanel = new JPanel();

	private JPanel northPanel_n = new JPanel();
	private JPanel northPanel_s = new JPanel();
	private JPanel northPanel_e = new JPanel();
	private JPanel northPanel_w = new JPanel();
	private JPanel northPanel_c = new JPanel();

	private JPanel northPanel_c_n = new JPanel();
	private JPanel northPanel_c_c = new JPanel();
	private JPanel northPanel_c_e = new JPanel();
	private JPanel northPanel_c_w = new JPanel();
	private JPanel northPanel_c_s = new JPanel();

	private JPanel contentPanel = new JPanel();
	private String currentPath = null;

	private JTextField jtf = new JTextField();
	private JPanel textfieldPanel = new JPanel();
	private JButton navigateButton = new JButton("Find");
	private JButton openDatabasesButton = new JButton("Open");

	private JScrollPane js;
	private JTree tree;

	public static Vector<DB4oFile> db4oFiles = new Vector<DB4oFile>();

	public static Vector<DB4oConnection> dbcv = new Vector<DB4oConnection>();
	//public static Hashtable<String, DB4oConnection> dbch = new Hashtable<String, DB4oConnection>();
	public static int recursionDepth = 0;
	public static int recursionDepthLimit = 7;

	public DB4oObjectViewer() {

	
		this.setTitle("Object Database Viewer (Java)");
		this.setContentPane(contentPanel);
		this.addWindowListener(this);
		this.setSize(new Dimension(1000, 800));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
		this.getContentPane().add(westpanel, BorderLayout.WEST);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);

		northPanel.setLayout(new BorderLayout());
		northPanel.add(northPanel_n, BorderLayout.NORTH);
		northPanel.add(northPanel_w, BorderLayout.WEST);
		northPanel.add(northPanel_e, BorderLayout.EAST);
		northPanel.add(northPanel_c, BorderLayout.CENTER);
		northPanel.add(northPanel_s, BorderLayout.SOUTH);

		northPanel_c.setLayout(new BorderLayout());
		northPanel_c.add(northPanel_c_n, BorderLayout.NORTH);
		northPanel_c.add(northPanel_c_w, BorderLayout.WEST);
		northPanel_c.add(northPanel_c_e, BorderLayout.EAST);
		northPanel_c.add(northPanel_c_c, BorderLayout.CENTER);
		northPanel_c.add(northPanel_c_s, BorderLayout.SOUTH);

		northPanel_c_c.add(textfieldPanel);

		textfieldPanel.add(navigateButton);
		textfieldPanel.add(jtf);
		textfieldPanel.add(openDatabasesButton);
		jtf.setText(DB4oModel.prePath);
		folderOfDatabases = new MyHomeFolder(DB4oModel.prePath);
		jtf.setPreferredSize(new Dimension(500, 25));
		jtf.setEditable(false);

		navigateButton.addActionListener(this);
		openDatabasesButton.addActionListener(this);

		centerPanel.setLayout(new BorderLayout());

		centerPanel.add(new JScrollPane(mtp), BorderLayout.CENTER);

		buildTree();
		js.setPreferredSize(new Dimension(300, 1500));
		buildMainTab();

	}

	private void buildMainTab() {

		mtp.addMainTab();

	}

	private void buildTree() {

		westpanel.removeAll();
		MyDefaultMutableTreeNode root = new MyDefaultMutableTreeNode("DB4o Data", folderOfDatabases);

		// MyTreeModel treeModel = new MyTreeModel(db);

		// DefaultMutableTreeNode child1 = new
		// DefaultMutableTreeNode("Child 1");
		// root.add(child1);
		// DefaultMutableTreeNode child2 = new
		// DefaultMutableTreeNode("Child 2");
		// root.add(child2);
		
		recursionDepth = 0;

		for (DB4oFile f : db4oFiles) {

			MyDefaultMutableTreeNode child1 = new MyDefaultMutableTreeNode("DB4o File: " + f.getName(), f);
			root.add(child1);

			String filePath = f.getAbsolutePath();
			DB4oConnection dbc = MyConnections.getConnection(filePath);
			dbcv.add(dbc);
			
			String className = f.getName().replace(".db4o", "");
			
			Vector<Class<?>> vectorOfUniqueClassesTemp = dbc.getListOfAllSpecificModelClassesInDB(className);
//			Vector<Class<?>> vectorOfUniqueClassesTemp = dbc.getListOfAllModelClassesInDB();
//			Vector<Class<?>> vectorOfUniqueClassesTemp = dbc.getListOfUniqueClassesInDB();
			
			boolean seeIfOk = processForSingleDB4oModelTypePerDB(f, vectorOfUniqueClassesTemp);
//			boolean seeIfOk = processForMultipleTypesPerDB(f, vectorOfUniqueClassesTemp);
		
			if(!seeIfOk){
				JOptionPane.showMessageDialog(null, "Error processing data in this file: " + f.getAbsolutePath());
				return;
			}
			
			
			for (Class<?> c : vectorOfUniqueClassesTemp) {

				//DB4oModel.hcd.put((Class<DB4oModel>) c, dbc.getDbci());
				MyDefaultMutableTreeNode child2 = new MyDefaultMutableTreeNode(c.getSimpleName() + "(DB4oModel)",c);
				child1.add(child2);
				recursionDepth = 0;
				findAllFieldsAndAddThemAsSubNodes(c,child2,true,recursionDepth);
				
			}
		}

		// tree = new JTree(root);
		tree = new JTree(root);

		MouseListener ml = new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				JTree tree = (JTree) e.getSource();
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				tree.setSelectionRow(selRow);
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

				if (selPath == null) {

					System.out.println("Selpath was null");
					return;
				}

				MyDefaultMutableTreeNode tn = (MyDefaultMutableTreeNode) selPath
						.getLastPathComponent();

				if (tn == null) {

					JOptionPane.showMessageDialog(null,
							"MyDefaultMutableTreeNode was null");
					return;
				}

				Object o = tn.getObjectInNode();

				if (o == null) {

					JOptionPane.showMessageDialog(null,
							"Object in node was null");
					return;
				}

				boolean isLeftClick = SwingUtilities.isLeftMouseButton(e);

				if (selRow != -1) {
					if (isLeftClick && e.getClickCount() == 1) {
						mySingleClick(selRow, selPath);
					} else if (isLeftClick) {

//						if (!vectorOfUniqueClasses.contains(o)) {
//							System.out.println("not a DB4oModel class...");
//							return;
//						}
						myDoubleClick(o, selRow, selPath);
					} else {

						createPopupMenu(o, tree, e.getX(), e.getY());

					}
				}
			}

			private void myDoubleClick(Object o, int selRow, TreePath selPath) {

				System.out.println(selRow + "----" + selPath);

				if(o instanceof Class<?>){
					
					if(DB4oModel.class.isAssignableFrom((Class<?>) o)){
						
						Class<DB4oModel> c = null;
						try {

							c = ((Class<DB4oModel>)o);

						
							if(MyTabbedPane.hclass.get(c) != null){
								
								MyDB4oTabbedView mdtv = MyTabbedPane.hclass.get(c);
								mtp.getJtp().setSelectedComponent(mdtv);
								return;
							}
							
//							DB4oConnection dbc = dbch.get(c.getName());
							
							DB4oConnection dbc = MyConnections.getConnection(DB4oModel.prePath + c.getSimpleName() + ".db4o");

//							JTableData jta = dbc.getTableDataForClass(c);
							JTableData jta = null;
							try {
								jta = dbc.getTableDataForClassAndSuperClasses(c);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mtp.addTabForOnlyDataView(c, jta);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				
			}

			private void mySingleClick(int selRow, TreePath selPath) {
				System.out.println(selRow + "----" + selPath);

			}
		};
		tree.addMouseListener(ml);

		westpanel.setLayout(new BorderLayout());
		westpanel.add(js = new JScrollPane(tree),BorderLayout.CENTER);
		//westpanel.setPreferredSize(new Dimension(500,3000));
		js.setPreferredSize(new Dimension(350,3000));
		westpanel.setMaximumSize(new Dimension(400,2900));
		westpanel.revalidate();
		westpanel.repaint();
	}

	private boolean processForMultipleTypesPerDB(DB4oFile f,
			Vector<Class<?>> vectorOfUniqueClassesTemp) {
		

		Vector<Class<DB4oModel>> db4cv = new Vector<Class<DB4oModel>>();
		
		if(vectorOfUniqueClassesTemp.size() > 1){
			
			for(Class<?> c: vectorOfUniqueClassesTemp){
				if(DB4oModel.class.isAssignableFrom(c)){
					db4cv.add((Class<DB4oModel>) c);
				}
			}
			if(db4cv.size() > 1){
			JOptionPane.showMessageDialog(null, "Error: More than one DB4oModel class in DB4oFile with name:\n" + f.getAbsolutePath());
			return false;
			}
		}
		
	
		return true;
	}

	private boolean processForSingleDB4oModelTypePerDB(DB4oFile f, Vector<Class<?>> vectorOfUniqueClassesTemp) {
		

		Class<DB4oModel> db4c = null;
		
		if(vectorOfUniqueClassesTemp.size() > 1){
			JOptionPane.showMessageDialog(null, "Error: More than one DB4oModel class in DB4oFile with name:\n" + f.getAbsolutePath());
			return false;
		}
		
		if(vectorOfUniqueClassesTemp.size() > 0){
			Class<?> c = vectorOfUniqueClassesTemp.get(0);
			if(!DB4oModel.class.isAssignableFrom(c)){
				JOptionPane.showMessageDialog(null, "Error: There is a class in the DB that is not a subclass of DB4oModel, in file:\n" + f.getAbsolutePath());
				return false;
			}
			db4c = (Class<DB4oModel>) c;
		}
		
		else{
			//we must use this if the database files exist but no records/objects are stored
			try {
				String className = f.getName().replace(".db4o", "");
				db4c = (Class<DB4oModel>) Class.forName("models." + className);
				
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("POTENTIAL PROBLEM: Have to use file to find DB4oModel class...");
			vectorOfUniqueClassesTemp.add(db4c);
		}
		
		return true;
	}

	private void findAllFieldsAndAddThemAsSubNodes(Class<?> cIn,
			MyDefaultMutableTreeNode child, boolean recurseThroughSuperClasses, int recursionDepth) {
	
		
		recursionDepth++;
		
		if(recursionDepth > recursionDepthLimit){
			recursionDepth = 0;
			return;
		}
		
		Class<DB4oModel> c = (Class<DB4oModel>) cIn;
		
		
		Field[] fs = c.getDeclaredFields();
		
		for(Field f: fs){
			
			MyDefaultMutableTreeNode mdmt = new MyDefaultMutableTreeNode(f.getName() + " (" + f.getType().getSimpleName() + ")",f.getType());
			child.add(mdmt);
			//recurse
			if(checkForRecursionClasses(f.getType())){
			findAllFieldsAndAddThemAsSubNodes(f.getType(),mdmt,false,recursionDepth);
			}

		}
		
		//we only go through superclasses once for DB4oModel subclass
		if(recurseThroughSuperClasses){
		Class<?> superClass = c;
		
		while((superClass = (Class<?>) superClass.getGenericSuperclass())  != null){
		
		Field[] superClassFields = superClass.getDeclaredFields();
		
		for(Field f: superClassFields){
			
			MyDefaultMutableTreeNode mdmt = new MyDefaultMutableTreeNode(f.getName() + " (" + f.getType().getSimpleName() + ")",f.getType());
			child.add(mdmt);
			//recurse
			if(checkForRecursionClasses(f.getType())){
			findAllFieldsAndAddThemAsSubNodes(f.getType(),mdmt,false,recursionDepth);
			}
		}
		}
		}
		
	}

	private boolean checkForRecursionClasses(Class<?> type) {
		
		if(type.equals(String.class)){
			return false;
		}
		if(type.equals(Integer.class)){
			return false;
		}
		if(type.equals(Date.class)){
			return false;
		}
		if(type.equals(Boolean.class)){
			return false;
		}
		if(type.equals(Hashtable.class)){
			return false;
		}
		if(type.equals(Long.class)){
			return false;
		}
		
		return true;
	}

	private void createPopupMenu(Object o, JTree tree, int x, int y) {

		JPopupMenu popup = new JPopupMenu();

		if (o instanceof Class<?>) {

			JMenuItem item1 = new JMenuItem("Open Data");
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

		} else if (o instanceof DB4oFile) {

			JMenuItem item1 = new JMenuItem("See DB4oFile info");
			JMenuItem item2 = new JMenuItem("Delete DB4oFile");
			JMenuItem item3 = new JMenuItem("Copy DB4oFile");

			popup.add(item1);
			popup.add(item2);
			popup.add(item3);

			item1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

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

		}

		else if (o instanceof MyHomeFolder) {

			JMenuItem item1 = new JMenuItem("See database info");
			JMenuItem item2 = new JMenuItem("Switch databases");
			JMenuItem item3 = new JMenuItem("Properties");

			popup.add(item1);
			popup.add(item2);
			popup.add(item3);

			item1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

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

		}

		popup.show(tree, x, y);

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {

		String[] choices = { "Close", "Cancel" };

		int choice = JOptionPane.showOptionDialog(null, "Close Program?",
				"Close?", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, choices, "Cancel");

		if (choice > 0) {
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			return;
		}
		
		
		closeAllConnections();
		System.exit(0);
	}

	private void closeAllConnections() {
		//close all connections
		for(DB4oConnection dbc: dbcv){
			dbc.closeConnection();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == navigateButton) {

			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File folderOfDatabasesTemp = fc.getSelectedFile();
				if (folderOfDatabasesTemp != null) {
					folderOfDatabases = new MyHomeFolder(
							folderOfDatabasesTemp.getAbsolutePath());
					jtf.setText(folderOfDatabases.getAbsolutePath());
				}

			} else {
				return;
			}

		}

		else if (arg0.getSource() == openDatabasesButton) {

			if (folderOfDatabases == null) {
				JOptionPane.showMessageDialog(null, "No directory selected.");
				return;
			}

			if (folderOfDatabases.getAbsolutePath().equals(currentPath)) {

				String[] choices = { "Yes", "No, Cancel" };

				int choice = JOptionPane.showOptionDialog(null,
						"Refresh database connections?", "Refresh current database?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, choices, "Cancel");

				if (choice > 0) {
					return;
				}

			}
			else{
				
				String[] choices = { "Yes", "No, Cancel" };

				int choice = JOptionPane.showOptionDialog(null,
						"Close all connections and open database for new directory/project?", "Open new database?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, choices, "Cancel");

				if (choice > 0) {
					return;
				}
				
			}
			
			closeAllConnections();
			
			Iterator<DB4oConnection> dbcIterator = dbcv.iterator();
			
			while(dbcIterator.hasNext()){
				
				DB4oConnection dbc = dbcIterator.next();
				dbc.getDb().close();
				dbcIterator.remove();
			}
			
			
			dbcv.removeAllElements();

			Vector<DB4oFile> db4oFiles = new Vector<DB4oFile>();

			File[] listOfFiles = folderOfDatabases.listFiles();

			for (File f : listOfFiles) {

				if (f.exists()) {
					if (f.getName().endsWith(".db4o")) {
						db4oFiles.add(new DB4oFile(f.getAbsolutePath()));
						System.out.println(f.getAbsolutePath());
					}

				}

			}
			if (db4oFiles.size() < 1) {
				JOptionPane
						.showMessageDialog(null,
								"No DB4o database files found in the directory you selected.");
				return;

			}
			
			DB4oObjectViewer.db4oFiles = db4oFiles;
			currentPath = folderOfDatabases.getAbsolutePath();
			buildTree();

		}

	}

	public MyTabbedPane getTp() {
		return mtp;
	}

	public void setTp(MyTabbedPane tp) {
		this.mtp = tp;
	}

}
