package models;

import java.lang.reflect.Field;
import java.util.Vector;


public class DB4oSequence extends DB4oModel{

	private Integer sequenceNumber = 0;
	private String sequenceName = null;
	
	public DB4oSequence(DB4oSequenceInfo dsi) {
	
		super();
		this.setSequenceName(dsi.getSequenceName());

	}

	public Integer getNextSequenceNumber(){
		
		Integer seq = sequenceNumber;
		sequenceNumber++;
		save(true);
		return seq;
	}
	
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}


	public String getSequenceName() {
		return sequenceName;
	}


	public void setSequenceName(String name) {
		this.sequenceName = name;
	}

	@Override
	public Vector<Object> getFieldDropDownValues(Field f) {
		// TODO Auto-generated method stub
		return null;
	}


}
