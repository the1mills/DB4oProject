package models;

import java.lang.reflect.Field;
import java.util.Vector;

import jtableStuff.JTableData;
import annotations.EditableField;
import annotations.ViewableField;

public class MyShitFuckingModel extends DB4oModel {
	
	@ViewableField
	@EditableField
	private String name = "fart";
	@ViewableField
	@EditableField
	private Integer bookNumber = 5;
	@ViewableField
	@EditableField
	private String sucker = "shit";
	@ViewableField
	private JTableData x = null;
	@ViewableField
	private String monkey = "ape";

	public MyShitFuckingModel() {
		
		super();
		canAddNewRecordInObjectViewer = true;
		canEditRecordInObjectViewer = false;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(Integer bookNumber) {
		this.bookNumber = bookNumber;
	}


	public String getSucker() {
		return sucker;
	}


	public void setSucker(String sucker) {
		this.sucker = sucker;
	}


	public String getMonkey() {
		return monkey;
	}


	public void setMonkey(String monkey) {
		this.monkey = monkey;
	}


	public JTableData getX() {
		return x;
	}


	public void setX(JTableData x) {
		this.x = x;
	}


	@Override
	public Vector<Object> getFieldDropDownValues(Field f) {
Vector<Object> vo = new Vector<Object>();
		
		try {
			if(f == DB4oModel.class.getField("userDefinedFileName")){
				vo.add(new String("candy"));
				vo.add(new String("death"));
				return vo;
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
