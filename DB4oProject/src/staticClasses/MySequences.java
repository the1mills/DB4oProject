package staticClasses;
import java.util.Hashtable;

import models.DB4oConnectionInfo;
import models.DB4oSequence;
import models.DB4oSequenceInfo;
import utilities.DB4oConnection;
import utilities.DB4oSequenceConnection;


public class MySequences {
	
	public static Hashtable<DB4oSequenceInfo,DB4oSequence> seqHash = new Hashtable<DB4oSequenceInfo,DB4oSequence>();

	public MySequences() {
		
	}
	
	public static DB4oSequence getSequence(DB4oConnectionInfo dbci, DB4oSequenceInfo dsi){
		
		DB4oSequence dseq = seqHash.get(dsi);
		
		if(dseq == null){
			//find sequence in database
			DB4oSequenceConnection dc =/* MyConnections.getSequenceConnection(dsi);*/ null;
			dseq = (DB4oSequence) dc.getObjectWithClassAndAttributes(DB4oSequence.class,"sequenceName",dsi.getSequenceName());
			if(dseq != null){
				seqHash.put(dsi, dseq);
			}
		    else{	
			//if sequence is not in database, create a new sequence
			System.out.println("sequence was null twice ---> it could not be found in Java memory or DB, creating new sequence and saving new sequence.");
			dseq = new DB4oSequence(dsi);
			dseq.save(true);
			dseq.getDb4oConn().commit();
			seqHash.put(dsi, dseq);
		}
		
		}
		return dseq;
	}



}
