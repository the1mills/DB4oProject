package models;

import java.lang.reflect.Field;
import java.util.Vector;

import annotations.EditableField;
import annotations.ViewableField;


public class MySecondModel extends DB4oModel {
	
	@ViewableField
	@EditableField
	private String life = "l.i.f.e.";
	@ViewableField
	@EditableField
	private String death = "death";

	public MySecondModel() {
		
		super();
		canAddNewRecordInObjectViewer = true;
		canEditRecordInObjectViewer = true;
//		this.setDatabaseConnectionInfo(new DB4oConnectionInfo(DB4oModel.prePath + this.getClass().getSimpleName() + "Connection"+".db4o"));
//			this.setSeqName(this.getClass().getName() + "Sequence");
		
	}

	public String getLife() {
		return life;
	}

	public void setLife(String life) {
		this.life = life;
	}

	public String getDeath() {
		return death;
	}

	public void setDeath(String death) {
		this.death = death;
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
