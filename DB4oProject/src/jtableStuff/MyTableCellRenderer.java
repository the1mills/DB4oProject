package jtableStuff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyTableCellRenderer extends DefaultTableCellRenderer {

	   private static final long serialVersionUID = 1L;

	   public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int col) {

	     JPanel p = new JPanel();
	     p.setLayout(new BorderLayout());
	     
	     Object valueAt = table.getModel().getValueAt(row, col);
	     
	     if(valueAt != null){
	      String s = valueAt.toString();

	      JLabel jl = new JLabel(s);
	      p.add(jl,BorderLayout.NORTH);
	     }
	     else{
	    	 JLabel jl = new JLabel("(null)");
		      p.add(jl,BorderLayout.NORTH);
	     }
	      
	      if(isSelected){
	    	  p.setBackground(Color.pink.darker());
	      }
	      
	      return p;
	   }
	}