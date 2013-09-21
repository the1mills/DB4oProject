package models;

import java.lang.reflect.Field;
import java.util.Vector;

import annotations.EditableField;
import annotations.ViewableField;


public class FuckShitModel extends DB4oModel {
	
	@ViewableField
	@EditableField
	private Boolean boolDeath = false;
	@ViewableField
	@EditableField
	private String murder = "slippy sluppy";
	@ViewableField
	@EditableField
	private Integer fuckass = new Integer(0);

	public FuckShitModel() {
		
		super();
		canAddNewRecordInObjectViewer = true;
		canEditRecordInObjectViewer = true;
//		this.setDatabaseConnectionInfo(new DB4oConnectionInfo(DB4oModel.prePath + this.getClass().getSimpleName() + "Connection"+".db4o"));
//		this.setSeqName(this.getClass().getName() + "Sequence");
	}

	public String getMurder() {
		return murder;
	}


	public void setMurder(String murder) {
		this.murder = murder;
	}


	public Integer getFuckass() {
		return fuckass;
	}

	public void setFuckass(Integer fuckass) {
		this.fuckass = fuckass;
	}
	
	public Boolean getBoolDeath() {
		return boolDeath;
	}

	public void setBoolDeath(Boolean boolDeath) {
		this.boolDeath = boolDeath;
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
