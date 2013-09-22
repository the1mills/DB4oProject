package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jtableStuff.JTableData;
import jtableStuff.MyTableDataPanel;
import models.DB4oModel;

public class MyTabbedPane extends JPanel implements MouseListener {

	private static JTabbedPane jtp = null;
	public static Hashtable<Integer, MyDB4oTabbedView> h = new Hashtable<Integer, MyDB4oTabbedView>();
	public static Hashtable<Class<DB4oModel>, MyDB4oTabbedView> hclass = new Hashtable<Class<DB4oModel>, MyDB4oTabbedView>();

	public MyTabbedPane() {

		jtp = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT) {

		
		};

		this.setBackground(Color.orange);
		this.setLayout(new BorderLayout());
		this.add(jtp, BorderLayout.CENTER);
		jtp.addMouseListener(this);
	}

	public void addMainTab() {

		JPanel mainTab = new JPanel();
		//this should be made into a command line tool to create a new DB4o project in a particular directory
		mainTab.add(new JButton("Create New Project"));
		//this might not be necessary, only way I know to create new DB is to store an object with a particular class
		mainTab.add(new JButton("Create New Database"));
		mainTab.add(new JButton("Refactor Database"));
		mainTab.add(new JButton("BackUp Database"));
		mainTab.add(new JButton("Delete Database"));
		jtp.addTab("Main Tab", mainTab);
		revalidate();
		repaint();
	}

	public void addTabForOnlyDataView(Class<DB4oModel> c, JTableData jta) {

		MyTableDataPanel mtdp = new MyTableDataPanel(jta);
		MyDB4oTabbedView mdtv = new MyDB4oTabbedView(c);
		mdtv.addDataPanel(mtdp);
		jtp.addTab("data n stuff", mdtv);
		hclass.put((Class<DB4oModel>) c,  mdtv);
		mdtv.addTabComponent(mdtv,this);
		mdtv.addStandardNorthPanel();
		jtp.setSelectedComponent(mdtv);
		revalidate();
		repaint();
	}

	public void addTab(Class<DB4oModel> c) {

		MyDB4oTabbedView mdtv = new MyDB4oTabbedView(c);
		mdtv.addDataPaneWithClass((Class<DB4oModel>) c);
		jtp.addTab(c.getName(), mdtv);
		revalidate();
		repaint();
	}

	

	public JTabbedPane getJtp() {
		return jtp;
	}

	public void setJtp(JTabbedPane jtp) {
		this.jtp = jtp;
	}
	
	
	public void removeTab(int index) {

		MyDB4oTabbedView c = (MyDB4oTabbedView) jtp.getComponentAt(index);
		jtp.remove(c);
		revalidate();
		repaint();
	}

	public void removeTab(MyDB4oTabbedView c) {

		jtp.remove(c);
		revalidate();
		repaint();
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void removeTab(MyTableDataPanel mtdp) {
		// TODO Auto-generated method stub
		
	}

}
