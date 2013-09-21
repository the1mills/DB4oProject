package models;

public class DB4oSequenceInfo {
	
	private String sequenceName;

	public DB4oSequenceInfo(String sequenceName) {
		
		this.setSequenceName(sequenceName);
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	
	public String toString(){
		
		return sequenceName;
	}
	
	public boolean equals(Object o){
		
		if(o instanceof DB4oSequenceInfo){
			
			if(((DB4oSequenceInfo) o).getSequenceName().equalsIgnoreCase(this.getSequenceName())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
        return new Integer(1112);
    }

}
