package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import models.DB4oModel;
import staticClasses.DB4oProjUtils;
import annotations.EditableField;
import annotations.ViewableField;

public class EditSingleRecordView extends JDialog implements ActionListener {

	private Class<DB4oModel> db4c;
	private DB4oModel db4m;
	private Vector<Field> editableAndOrViewableFields = new Vector<Field>();
	private JPanel contentPanel;
	private JPanel centerPanel;
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel westPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private GridLayout gl;
	private JScrollPane js;
	private JButton doneButton = new JButton("Save Edits to Object");
	private JButton cancelButton = new JButton("Cancel");
	private Hashtable<JTextField, Field> jtfHash = new Hashtable<JTextField, Field>();
	private JLabel titleLabel;
	private MyDB4oTabbedView mdtv;
	private JLabel errorMessageLabel;
	private JPanel mainCenterPanel;
	private MouseListener ml;
	private Border standardJTextFieldBorder;

	public EditSingleRecordView(DB4oModel db4m, MyDB4oTabbedView mdtv) {

		this.setModal(true);
		this.setTitle("Edit Record");
		this.setMdtv(mdtv);
		this.setDb4m(db4m);
		this.db4c = mdtv.getDb4oClass();
		this.setLocationRelativeTo(null);
		findEditableAndViewableFields();
		this.setContentPane(setContentPanel(new JPanel()));
		this.getContentPane().setLayout(new BorderLayout());
		centerPanel = new JPanel();
		js = new JScrollPane(centerPanel);
		this.getContentPane().add(mainCenterPanel = new JPanel(),
				BorderLayout.CENTER);
		mainCenterPanel.setLayout(new BorderLayout());
		mainCenterPanel.add(js, BorderLayout.CENTER);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.add(setTitleLabel(new JLabel("Edit Record for object: " + db4m
				+ " with db4o internal id = " + db4m.getDb4oInternalId())));
		this.getTitleLabel().setFont(new Font("Helvetica", Font.BOLD, 16));
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.add(doneButton);
		doneButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
		this.getContentPane().add(westPanel, BorderLayout.WEST);
		centerPanel.setLayout(setGl(new GridLayout()));
		gl.setRows(Math.max(10, editableAndOrViewableFields.size()));

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

		for (Field f : editableAndOrViewableFields) {

			JPanel p = new JPanel();
			p.setBackground(Color.LIGHT_GRAY);
			JLabel jl = new JLabel(f.getName());
			jl.setPreferredSize(new Dimension(200, 30));
			p.add(jl);
			JTextField jtf = new JTextField();
			if (standardJTextFieldBorder == null) {
				standardJTextFieldBorder = jtf.getBorder();
			}
			jtf.addMouseListener(ml);
			boolean isEditable = f.isAnnotationPresent(EditableField.class);
			if (isEditable) {
				jtf.setEditable(true);
			} else {
				jtf.setEditable(false);
			}

			Method m = null;

			try {
				m = db4m.getClass().getMethod(
						"get"
								+ DB4oProjUtils.INSTANCE.capitalizeString(f
										.getName()), null);
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Object o = m.invoke(db4m, null);
				if (o != null) {
					jtf.setText(o.toString());
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

			jtf.setPreferredSize(new Dimension(300, 30));
			p.add(jtf);
			centerPanel.add(p, gl);
			jtfHash.put(jtf, f);
		}

		// this.setMinimumSize(new Dimension(400,600));

		// this.setMaximumSize(new Dimension(1000,700));
		// this.pack();
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.red.darker());
		mainCenterPanel.add(errorMessageLabel, BorderLayout.SOUTH);
		this.setSize(700, 700);
	}

	private void findEditableAndViewableFields() {
		Field[] fs = db4c.getDeclaredFields();

		for (Field f : fs) {
			boolean isEditable = f.isAnnotationPresent(EditableField.class);
			boolean isViewable = f.isAnnotationPresent(ViewableField.class);
			if (isEditable || isViewable) {
				f.setAccessible(true);
				editableAndOrViewableFields.add(f);
			}
		}

		Class<?> superClass = (Class<?>) db4c;

		while ((superClass = (Class<?>) superClass.getGenericSuperclass()) != null) {

			Field[] superClassFields = superClass.getDeclaredFields();

			for (Field f : superClassFields) {

				boolean isEditable = f.isAnnotationPresent(EditableField.class);
				boolean isViewable = f.isAnnotationPresent(ViewableField.class);
				if (isEditable || isViewable) {
					f.setAccessible(true);
					editableAndOrViewableFields.add(f);
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
		return editableAndOrViewableFields;
	}

	public void setEditableFields(Vector<Field> editableFields) {
		this.editableAndOrViewableFields = editableFields;
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
			
			boolean success = 	updateExistingObjectAndSave();
			if (success) {
				this.dispose();
			}
			
		}

	}

	private boolean updateExistingObjectAndSave() {

		Enumeration<JTextField> e = jtfHash.keys();

		while (e.hasMoreElements()) {
			JTextField jtf = e.nextElement();
			String s = jtf.getText().trim();
			Object o = null;
			Field f = jtfHash.get(jtf);

			boolean isEditable = f.isAnnotationPresent(EditableField.class);
			if (!isEditable) {
				continue;
			}

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
				try {
					o = new Date();
				} catch (Exception e1) {
					createErrorMessageLabel(jtf, e1);
					return false;
				}
			} else {
					try {
						o = new String(s);
					} catch (Exception e1) {
						createErrorMessageLabel(jtf, e1);
						return false;
					}
				
			}

			Method m = null;
			try {
				m = db4m.getClass().getMethod(
						"set"
								+ DB4oProjUtils.INSTANCE.capitalizeString(f
										.getName()), fieldType);
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				m.invoke(db4m, o);
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

		}

		db4m.save(true);
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

	public DB4oModel getDb4m() {
		return db4m;
	}

	public void setDb4m(DB4oModel db4m) {
		this.db4m = db4m;
	}

	public JPanel getMainCenterPanel() {
		return mainCenterPanel;
	}

	public void setMainCenterPanel(JPanel mainCenterPanel) {
		this.mainCenterPanel = mainCenterPanel;
	}

	public MouseListener getMl() {
		return ml;
	}

	public void setMl(MouseListener ml) {
		this.ml = ml;
	}

}
