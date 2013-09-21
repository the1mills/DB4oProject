package models;

public class DB4oConnectionInfo {
	
	String connectionName = null;

	
	
	public DB4oConnectionInfo(String name){
		this.connectionName = name;
	}

	
	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	
	public String toString(){
		
		return connectionName;
	}
	
	public boolean equals(Object o){
		
		if(o instanceof DB4oConnectionInfo){
			
			if(((DB4oConnectionInfo) o).getConnectionName().equalsIgnoreCase(this.getConnectionName())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
        return new Integer(1111);
    }
}
