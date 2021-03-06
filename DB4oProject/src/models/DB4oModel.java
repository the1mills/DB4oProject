package models;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.db4o.Db4oEmbedded;
import com.db4o.config.ConfigScope;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.Db4oUUID;

import staticClasses.MyConnections;
import utilities.DB4oConnection;
import utilities.DB4oInternalId;
import annotations.EditableField;
import annotations.ViewableField;

public abstract class DB4oModel {


	@ViewableField
	public Boolean canAddNewRecordInObjectViewer = true;
	@ViewableField
	public Boolean canEditRecordInObjectViewer;
	//public transient static String prePath = "C:\\Users\\alex\\Desktop\\db4oFiles\\";
	public transient static String prePath = "C:\\Users\\denman\\Desktop\\db4oFiles\\";
	@ViewableField
	private DB4oConnectionInfo databaseConnectionInfo;
	//private DB4oSequenceInfo seqInfo;
	//private transient DB4oConnection db4oConn;
	//private DB4oSequence db4oSeq;
	@ViewableField
	private transient Db4oUUID uuid;
	@ViewableField
	private DB4oInternalId db4oInternalId;
	@ViewableField
	@EditableField
	private String userDefinedFileName;
	@ViewableField
	private Date dateCreated;

	
	public DB4oModel() {
		
//		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
//		configuration.file().generateUUIDs(ConfigScope.GLOBALLY);
		
		String conn = prePath + this.getClass().getSimpleName() + ".db4o";
		this.setDatabaseConnectionInfo(new DB4oConnectionInfo(conn));
	//	this.setSeqInfo(new DB4oSequenceInfo(conn + "Sequence.db4o"));
	}
	
	public abstract Vector<Object> getFieldDropDownValues(Field f);


	public final void save(boolean commit) {


		if (dateCreated == null) {
			dateCreated = new Date();
		}

//		if (!(this instanceof DB4oSequence) && id == null) {
//			id = this.getDb4oSeq().getNextSequenceNumber();
//		}

		try {
			this.getDb4oConn().store(this);
		} catch (Exception e) {
			//this.setId(null);
			e.printStackTrace();
		}
		
		if(commit){
		commit();
		}

	}

	public final void delete(boolean commit) {

		this.getDb4oConn().delete(this);
		if(commit){
		commit();
		}
	}
	
	public final void commit() {

		this.getDb4oConn().commit();
	}


	public final DB4oInternalId getDb4oInternalId() {
		if(db4oInternalId == null){
			db4oInternalId = new DB4oInternalId();
			db4oInternalId.setId(this.getDb4oConn().getDb().ext().getID(this));
		}
		return db4oInternalId;
	}

	public final void setDb4oInternalId(Integer id) {
		//this.db4oInternalId = id;
	}

	public final String getUserDefinedFileName() {
		return userDefinedFileName;
	}

	public final void setUserDefinedFileName(String userDefinedFileName) {
		this.userDefinedFileName = userDefinedFileName;
	}

	public final Date getDateCreated() {
		return dateCreated;
	}

	public final void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
//	public DB4oSequenceInfo getSeqInfo() {
//		return seqInfo;
//	}
//
//	public void setSeqInfo(DB4oSequenceInfo seqInfo) {
//		this.seqInfo = seqInfo;
//	}

	public static String getPrePath() {
		return prePath;
	}

	public static void setPrePath(String prePath) {
		DB4oModel.prePath = prePath;
	}

	public final DB4oConnection getDb4oConn() {
		return MyConnections.getConnection(databaseConnectionInfo); 
	}

//	public final DB4oSequence getDb4oSeq() {
//		return MySequences.getSequence(databaseConnectionInfo,seqInfo);
//	}


	public final DB4oConnectionInfo getDatabaseConnectionInfo() {
		return databaseConnectionInfo;
	}

	public final void setDatabaseConnectionInfo(
			DB4oConnectionInfo databaseConnectionInfo) {
		this.databaseConnectionInfo = databaseConnectionInfo;
	}

	
	public Boolean getCanAddNewRecordInObjectViewer() {
		return canAddNewRecordInObjectViewer;
	}


	public Boolean getCanEditRecordInObjectViewer() {
		return canEditRecordInObjectViewer;
	}

	public void setCanEditRecordInObjectViewer(Boolean canEditRecordInObjectViewer) {
		this.canEditRecordInObjectViewer = canEditRecordInObjectViewer;
	}

	public Db4oUUID getUuid() {
		if(uuid == null){
			uuid = this.getDb4oConn().getDb().ext().getObjectInfo(this).getUUID();
		}
		return uuid;
	}

	public void setUuid(Db4oUUID uuid) {
		this.uuid = uuid;
	}

}
