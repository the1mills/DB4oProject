package views;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.util.Vector;

import javax.swing.JDialog;

import models.DB4oModel;

public class EditMultipleRecordsView extends JDialog {

	public EditMultipleRecordsView(Vector<DB4oModel> db4oModelObjectVector, MyDB4oTabbedView myDB4oTabbedView) {
		
		this.setModal(true);
	}


}
