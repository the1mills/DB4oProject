package utilities;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import models.DB4oSequenceInfo;

public class DB4oSequenceConnection extends DB4oConnection {


	DB4oSequenceInfo dsi = null;
	
	public DB4oSequenceConnection(DB4oSequenceInfo dsi) {
		
		this.dsi = dsi;
	}

	public DB4oSequenceInfo getDsi() {
		return dsi;
	}

	public void setDsi(DB4oSequenceInfo dsi) {
		this.dsi = dsi;
	}
	
	public void openNewDb() {
		db = Db4oEmbedded.openFile(getDsi().getSequenceName());
	}

	public ObjectContainer getDb() {

		if (db == null) {
			System.out.println("db was null in " + dsi
					+ " connection. Had to create new DB object.");
			db = Db4oEmbedded.openFile(dsi.getSequenceName());
		}
		return db;
	}
}
