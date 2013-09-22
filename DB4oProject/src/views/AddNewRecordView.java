package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import models.DB4oModel;
import staticClasses.DB4oProjUtils;
import annotations.EditableField;

public class AddNewRecordView extends JDialog implements ActionListener {

	private Class<DB4oModel> db4c;
	private Vector<Field> editableFields = new Vector<Field>();
	private JPanel contentPanel;
	private JPanel centerPanel;
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel westPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private GridLayout gl;
	private JScrollPane js;
	private JButton doneButton = new JButton("Save New Record");
	private JButton cancelButton = new JButton("Cancel");
	private Hashtable<JTextField, Field> jtfHash = new Hashtable<JTextField, Field>();
	private JLabel titleLabel;
	private MyDB4oTabbedView mdtv;
	private JLabel errorMessageLabel;
	private JPanel mainCenterPanel;
	private MouseListener ml;
	private Border standardJTextFieldBorder = null;

	public AddNewRecordView(MyDB4oTabbedView mdtv) {
		this.setMdtv(mdtv);
		this.db4c = mdtv.getDb4oClass();
		this.setLocationRelativeTo(null);
		findEditableFields();
		this.setContentPane(setContentPanel(new JPanel()));
		this.getContentPane().setLayout(new BorderLayout());
		centerPanel = new JPanel();
		js = new JScrollPane(centerPanel);
		this.getContentPane().add(mainCenterPanel = new JPanel(),
				BorderLayout.CENTER);
		mainCenterPanel.setLayout(new BorderLayout());
		mainCenterPanel.add(js, BorderLayout.CENTER);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.add(setTitleLabel(new JLabel("Add New Record for Class: "
				+ mdtv.getDb4oClass().getSimpleName())));
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.add(doneButton);
		doneButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
		this.getContentPane().add(westPanel, BorderLayout.WEST);
		centerPanel.setLayout(setGl(new GridLayout()));
		gl.setColumns(1);
		gl.setRows(Math.max(10, editableFields.size()));
		gl.setHgap(10);
		gl.setVgap(10);

		ml = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if (arg0.getSource() instanceof JTextField) {
					((JTextField) arg0.getSource())
							.setBorder(standardJTextFieldBorder);
				}

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

		};

		for (Field f : editableFields) {

			// GridLayout glTemp = new GridLayout();
			// glTemp.setColumns(3);
			// JPanel p = new JPanel();
			// p.setLayout(glTemp);
			// p.setBackground(Color.LIGHT_GRAY);
			// JLabel jl1 = new JLabel(f.getName());
			// jl1.setPreferredSize(new Dimension(150,30));
			// JLabel jl2 = new JLabel("(" + f.getType().getSimpleName() + ")");
			// jl2.setPreferredSize(new Dimension(100,30));
			// p.add(jl1,glTemp);
			// JTextField jtf = new JTextField();
			// jtf.setPreferredSize(new Dimension(400,30));
			// p.add(jtf,glTemp);
			// p.add(jl2,glTemp);
			// centerPanel.add(p,gl);
			// jtfHash.put(jtf, f);

			JPanel p = new JPanel();
			p.setLayout(new BorderLayout());
			JPanel centerPanelTemp = new JPanel();
			p.add(centerPanelTemp, BorderLayout.CENTER);
			p.setBackground(Color.LIGHT_GRAY);
			JLabel jl1 = new JLabel(f.getName());
			jl1.setPreferredSize(new Dimension(150, 30));
			JLabel jl2 = new JLabel("(" + f.getType().getSimpleName() + ")");
			jl2.setPreferredSize(new Dimension(100, 30));
			centerPanelTemp.add(jl1);
			JTextField jtf = new JTextField();
			if (standardJTextFieldBorder == null) {
				standardJTextFieldBorder = jtf.getBorder();
			}
			jtf.addMouseListener(ml);
			jtf.setPreferredSize(new Dimension(400, 30));
			centerPanelTemp.add(jtf);
			centerPanelTemp.add(jl2);
			centerPanel.add(p, gl);
			jtfHash.put(jtf, f);
		}

		// this.setMinimumSize(new Dimension(400,600));
		// this.setMaximumSize(new Dimension(1000,800));
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.red.darker());
		mainCenterPanel.add(errorMessageLabel, BorderLayout.SOUTH);
		this.setSize(700, 900);
	}

	private void findEditableFields() {
		Field[] fs = db4c.getDeclaredFields();

		for (Field f : fs) {
			boolean isEditable = f.isAnnotationPresent(EditableField.class);
			if (isEditable) {
				f.setAccessible(true);
				editableFields.add(f);
			}
		}

		Class<?> superClass = (Class<?>) db4c;

		while ((superClass = (Class<?>) superClass.getGenericSuperclass()) != null) {

			Field[] superClassFields = superClass.getDeclaredFields();

			for (Field f : superClassFields) {

				boolean isEditable = f.isAnnotationPresent(EditableField.class);
				if (isEditable) {
					f.setAccessible(true);
					editableFields.add(f);
				}
			}
		}
	}

	public Class<DB4oModel> getDb4c() {
		return db4c;
	}

	public void setDb4m(Class<DB4oModel> db4c) {
		this.db4c = db4c;
	}

	public Vector<Field> getEditableFields() {
		return editableFields;
	}

	public void setEditableFields(Vector<Field> editableFields) {
		this.editableFields = editableFields;
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public JPanel setContentPanel(JPanel contentPanel) {
		this.contentPanel = contentPanel;
		return contentPanel;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public void setCenterPanel(JPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	public GridLayout getGl() {
		return gl;
	}

	public GridLayout setGl(GridLayout gl) {
		this.gl = gl;
		return gl;
	}

	public Hashtable<JTextField, Field> getJtfHash() {
		return jtfHash;
	}

	public void setJtfHash(Hashtable<JTextField, Field> jtfHash) {
		this.jtfHash = jtfHash;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == cancelButton) {
			this.dispose();
		}
		if (arg0.getSource() == doneButton) {
			if (!doDataValidation()) {
				return;
			}
			boolean success = createNewObjectAndSetValues();
			if (success) {
				this.dispose();
			}
		}

	}

	private boolean createNewObjectAndSetValues() {

		DB4oModel db4m = null;
		try {
			db4m = db4c.newInstance();
		} catch (InstantiationException e1) {
			createErrorMessageLabel(null, e1);
			JOptionPane.showMessageDialog(null,
					"Could not create new object \n " + e1.getMessage());
			return false;
		} catch (IllegalAccessException e1) {
			createErrorMessageLabel(null, e1);
			JOptionPane.showMessageDialog(null,
					"Could not create new object \n " + e1.getMessage());
			return false;
		}

		Enumeration<JTextField> e = jtfHash.keys();

		while (e.hasMoreElements()) {
			JTextField jtf = e.nextElement();
			String s = jtf.getText().trim();
			Object o = null;
			Field f = jtfHash.get(jtf);
			Class<?> fieldType = f.getType();

			if (fieldType.equals(Integer.class)) {
				try {
					o = Integer.parseInt(s);
				} catch (NumberFormatException e1) {
					createErrorMessageLabel(jtf, e1);
					return false;
				}
			} else if (fieldType.equals(Boolean.class)) {
				try {
					o = Boolean.parseBoolean(s);
				} catch (Exception e1) {
					createErrorMessageLabel(jtf, e1);
					return false;
				}
			} else if (fieldType.equals(Date.class)) {
				o = new Date();
			} else {
				o = new String(s);
			}

			Method m = null;
			try {
				m = db4m.getClass().getMethod(
						"set"
								+ DB4oProjUtils.INSTANCE.capitalizeString(f
										.getName()), fieldType);
			} catch (SecurityException e1) {
				createErrorMessageLabel(jtf, e1);
				return false;
			} catch (NoSuchMethodException e1) {
				createErrorMessageLabel(jtf, e1);
				return false;
			}

			try {
				m.invoke(db4m, o);
			} catch (IllegalArgumentException e1) {
				createErrorMessageLabel(jtf, e1);
				return false;
			} catch (IllegalAccessException e1) {
				createErrorMessageLabel(jtf, e1);
				return false;
			} catch (InvocationTargetException e1) {
				createErrorMessageLabel(jtf, e1);
				return false;
			}

		}

		db4m.save();
		mdtv.refreshView(db4c);
		return true;
	}

	private void createErrorMessageLabel(JTextField jtf, Exception e1) {
		// e1.printStackTrace();
		errorMessageLabel.setText(e1.getMessage() + " ---->"
				+ e1.getClass().getSimpleName());
		if (jtf != null) {
			jtf.setBorder(BorderFactory.createLineBorder(Color.red, 2));
		}
		this.repaint();
	}

	private boolean doDataValidation() {
		// TODO Auto-generated method stub
		return true;
	}

	public JLabel getTitleLabel() {
		return titleLabel;
	}

	public JLabel setTitleLabel(JLabel titleLabel) {
		this.titleLabel = titleLabel;
		return titleLabel;
	}

	public MyDB4oTabbedView getMdtv() {
		return mdtv;
	}

	public void setMdtv(MyDB4oTabbedView mdtv) {
		this.mdtv = mdtv;
	}

	public JLabel getErrorMessageLabel() {
		return errorMessageLabel;
	}

	public void setErrorMessageLabel(JLabel errorMessageLabel) {
		this.errorMessageLabel = errorMessageLabel;
	}

}
