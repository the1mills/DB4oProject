package views;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import models.DB4oFile;

public class MyDefaultMutableTreeNode extends DefaultMutableTreeNode {
	
	private String nodeViewName;
	private Object objectInNode = null;
	
	public MyDefaultMutableTreeNode(String viewName, Object o){
		
		this.setNodeViewName(viewName);
		this.setObjectInNode(o);
		
	}

	public Object getObjectInNode() {
		return objectInNode;
	}

	public void setObjectInNode(Object objectInNode) {
		this.objectInNode = objectInNode;
	}
	
	public String toString(){
		
		if(this.getNodeViewName() != null){
			return this.getNodeViewName();
		}
		
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

	public String getNodeViewName() {
		return nodeViewName;
	}

	public void setNodeViewName(String nodeViewName) {
		this.nodeViewName = nodeViewName;
	}

}
