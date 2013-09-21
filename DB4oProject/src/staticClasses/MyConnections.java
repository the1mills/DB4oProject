package staticClasses;

import java.util.Enumeration;
import java.util.Hashtable;

import models.DB4oConnectionInfo;
import models.DB4oFile;
import models.DB4oSequenceInfo;
import utilities.DB4oConnection;
import utilities.DB4oSequenceConnection;

public class MyConnections {

	public static Hashtable<DB4oSequenceInfo, DB4oSequenceConnection> dbSeqConn = new Hashtable<DB4oSequenceInfo, DB4oSequenceConnection>();
	public static Hashtable<DB4oConnectionInfo, DB4oConnection> dbConn = new Hashtable<DB4oConnectionInfo, DB4oConnection>();

	public MyConnections() {
		// TODO Auto-generated constructor stub
	}
	
	public static void printFuckingConnections(){
		
		Enumeration<?> k = dbConn.keys();
		
		while(k.hasMoreElements()){
			Object o = k.nextElement();
			DB4oConnection dbc = dbConn.get(o);
			System.out.println(dbc);
		}
	}

	public static DB4oConnection getConnection(DB4oFile file) {
		
		printFuckingConnections();
		
		DB4oConnectionInfo dbci = new DB4oConnectionInfo(file.getAbsolutePath());
		DB4oConnection dbc = dbConn.get(dbci);

		if (dbc == null) {
			dbc = new DB4oConnection(file);
			dbc.setDbci(dbci);
			dbConn.put(dbci, dbc);
		}
		return dbc;
	}

	
	public static DB4oConnection getConnection(DB4oConnectionInfo dbci) {

		
//		printFuckingConnections();
		DB4oConnection dbc = dbConn.get(dbci);

		if (dbc == null) {
			dbc = new DB4oConnection(dbci);
			dbc.setDbci(dbci);
			dbConn.put(dbci, dbc);
		}
		return dbc;
	}
	
	public static DB4oSequenceConnection getSequenceConnection(DB4oSequenceInfo dsi) {

		
//		printFuckingConnections();
		DB4oSequenceConnection dbc = dbSeqConn.get(dsi);

		if (dbc == null) {
			dbc = new DB4oSequenceConnection(dsi);
			dbSeqConn.put(dsi, dbc);
		}
		return dbc;
	}

}
