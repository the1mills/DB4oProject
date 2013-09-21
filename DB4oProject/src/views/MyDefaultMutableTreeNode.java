package views;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import models.DB4oFile;

public class MyDefaultMutableTreeNode extends DefaultMutableTreeNode {
	
	private Object objectInNode = null;
	
	public MyDefaultMutableTreeNode(Object o){
		
		super(o.toString());
		this.setObjectInNode(o);
		
	}

	public Object getObjectInNode() {
		return objectInNode;
	}

	public void setObjectInNode(Object objectInNode) {
		this.objectInNode = objectInNode;
	}
	
	public String toString(){
		
		if(objectInNode instanceof DB4oFile){
			return ((DB4oFile) objectInNode).getName();
		}
		if(objectInNode instanceof Class){
			return ((Class) objectInNode).getSimpleName();
		}
		if(objectInNode instanceof MyHomeFolder){
			return ((MyHomeFolder) objectInNode).getName();
		}
		else return super.toString();
	}

}
