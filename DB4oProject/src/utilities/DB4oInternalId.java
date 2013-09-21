package utilities;


public class DB4oInternalId {

	private Long id;
	
	
	public DB4oInternalId() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	public String toString(){
		return id.toString();
	}

}
