package utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.text.WordUtils;

import staticClasses.DB4oProjUtils;

import jtableStuff.JTableData;
import jtableStuff.MakeJTableFrame;
import jtableStuff.MyNullObjectClass;
import models.DB4oConnectionInfo;
import models.DB4oFile;
import models.DB4oModel;

import annotations.ViewableField;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class DB4oConnection {

	private DB4oFile db4oFile;
	private DB4oConnectionInfo dbci = null;
	protected ObjectContainer db = null;

	
	
	public DB4oConnection() {

//		if (db == null) {
//			this.openNewDb();
//		}

	}
	
	public DB4oConnection(String filename) {

		dbci = new DB4oConnectionInfo(filename);

		if (db == null) {
			openNewDb();
		}

	}
	
	public DB4oConnection(DB4oConnectionInfo dbci) {

		this.dbci = dbci;

		if (db == null) {
			openNewDb();
		}

	}
	
	public DB4oConnection(DB4oFile file) {

		this.db4oFile = file;
		this.dbci = new DB4oConnectionInfo(file.getAbsolutePath());

		if (db == null) {
			System.out.println("db was null, creating new DB.");
			openNewDb();
		}
	}
	

	public String toString() {
		return "Filename: " + dbci + ", DB (object container): " + db + ", DB4oFile: " + db4oFile;
	}



	public ObjectContainer getDb() {

		if (db == null) {
			System.out.println("db was null in " + dbci
					+ " connection. Had to create new DB object.");
			db = Db4oEmbedded.openFile(dbci.getConnectionName());
		}
		try{
			db.query();
		}
		catch(Exception e){
			System.out.println("db appears closed in " + dbci
					+ " connection. Had to create new DB object.");
			 db = Db4oEmbedded.openFile(dbci.getConnectionName());
		}
		return db;
	}

	public void setDb(ObjectContainer db) {
		this.db = db;
	}

	public void store(Object o) {

		this.getDb().store(o);
	}

	public void delete(Object o) {

		this.getDb().delete(o);
	}
	
	public void commit() {

		this.getDb().commit();

	}
	
	

	public Vector<Class<?>> getListOfUniqueClassesInDB() {

		Vector<Class<?>> allClasses = new Vector<Class<?>>();

		Query q = getDb().query();

		ObjectSet<Class<?>> x = q.execute();

		while (x.hasNext()) {

			try {
				Object o = x.next();
				Class<?> c = o.getClass();

				if (!allClasses.contains(c)) {
					allClasses.add(c);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		return allClasses;
	}
	
	
	public Vector<Class<?>> getListOfAllSpecificModelClassesInDB(String className) {

		Vector<Class<?>> allClasses = new Vector<Class<?>>();

		Class<DB4oModel> dbClass = null;
		try {
			 dbClass = (Class<DB4oModel>) Class.forName("models." + className);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(dbClass == null){
			return null;
		}
		
		Query q = getDb().query();
		q.constrain(dbClass);

		ObjectSet<Class<?>> x = q.execute();

		while (x.hasNext()) {

			try {
				Object o = x.next();
				Class<?> c = o.getClass();

				if (!allClasses.contains(c)) {
					allClasses.add(c);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		return allClasses;
	}
	
	public Vector<Class<?>> getListOfAllModelClassesInDB() {

		Vector<Class<?>> allClasses = new Vector<Class<?>>();

		Query q = getDb().query();
		q.constrain(DB4oModel.class);

		ObjectSet<Class<?>> x = q.execute();

		while (x.hasNext()) {

			try {
				Object o = x.next();
				Class<?> c = o.getClass();

				if (!allClasses.contains(c)) {
					allClasses.add(c);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		return allClasses;
	}

	public JTableData getThreeColumnListOfAllDBObjects() {

		Vector<Class<?>> columnTypes = new Vector<Class<?>>();
		columnTypes.add(String.class);
		columnTypes.add(Integer.class);
		columnTypes.add(Object.class);
		
		Object[] tableData = new Object[2];

		String[] columnNames = { "Object to String", "Object id",
				"Object Class" };

		ObjectSet<Class<?>> x = null;

		Query q = getDb().query();

		x = q.execute();

		Object[][] dataValues = new Object[x.size()][3];

		int i = 0;

		while (x.hasNext()) {

			Object obj = x.next();

			dataValues[i][0] = obj.toString();
			dataValues[i][1] = obj;
			dataValues[i][2] = obj.getClass().getName();

			i++;
		}

		return new JTableData(null, columnTypes, columnNames, dataValues);

		// MyStaticFields.mainFrame = new MakeJTable(columnNames,dataValues);
		// MyStaticFields.mainFrame.setVisible(true);
	}

	public void deleteFromDatabaseByClass(Class<?> clazz) {

	
		Query q = getDb().query();

		q.constrain(clazz);
		ObjectSet<?> x = q.execute();

		while (x.hasNext()) {
			getDb().delete(x.next());
		}

	}

	// native query

	public List<Object> getObjectsWithClass2() {

		List<Object> pilots = getDb().query(
				new com.db4o.query.Predicate<Object>() {
					public boolean match(Object pilot) {
						return pilot.getClass().equals(100);
					}
				});

		return pilots;
	}

	public Object getObjectWithClassAndAttributes(Class<?> c, String name,
			String value) {

		Object o = null;

		Query q = getDb().query();

		// Constrain query
		q.constrain(c);
		// q.descend(name).constrain(value);
		// q.descend(name).orderAscending();

		ObjectSet<Class<?>> x = q.execute();

		int i = 0;
		while (x.hasNext()) {
			o = x.next();
			i++;
		}
		if (i > 1) {
			return null;
		}
		return o;
	}


	public JTableData getTableDataForObjectsClass(Object o) {

		Vector<Class<?>> columnTypes = new Vector<Class<?>>();
		Field[] columnFields = o.getClass().getDeclaredFields();
		String[] columnNames = new String[columnFields.length];

		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columnFields[i].getName();
		}

		Query q = db.query();
		q.constrain(o);

		ObjectSet<Class<?>> x = q.execute();

		Object[][] dataValues = new Object[x.size()][columnNames.length];

		int i = 0;
		while (x.hasNext()) {

			Object obj = x.next();
			for (int j = 0; j < columnFields.length; j++) {
				
				columnTypes.add(columnFields[j].getType());
				try {
					String nameOfField = obj.getClass()
							.getDeclaredField(columnFields[j].getName())
							.getName();
					nameOfField = DB4oProjUtils.INSTANCE.capitalizeString(nameOfField);
					Method m = null;
					try {
						m = obj.getClass().getMethod("get" + nameOfField, null);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						dataValues[i][j] = new String(
								"null - no method exception.");
						continue;
					}
					Object temp = null;
					try {
						temp = m.invoke(obj, null);
						if (temp == null) {
							dataValues[i][j] = new MyNullObjectClass();
						} else {
							// dataValues[i][j] = temp.toString();
							dataValues[i][j] = temp;
						}

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
			i++;
		}

		JTableData jta = new JTableData(o, columnTypes, columnNames, dataValues);
		return jta;

	}

	public void openJTablFrameWithJTableData(JTableData jta) {

		MakeJTableFrame mainFrame = new MakeJTableFrame(jta);
		mainFrame.setVisible(true);

	}

	public JTableData getTableDataForClass(Class<?> c) {

		Field[] columnFields = c.getDeclaredFields();
		String[] columnNames = new String[columnFields.length + 1];
		Vector<Class<?>> columnTypes = new Vector<Class<?>>();

		int z = 0;
		for (z = 0; z < columnFields.length;z++) {
			columnNames[z] = columnFields[z].getName();
		}
		
		columnNames[z] = "Object Itself";

		Query q = getDb().query();
		q.constrain(c);

		ObjectSet<Class<?>> x = q.execute();

		Object[][] dataValues = new Object[x.size()][columnNames.length + 1];

		int i = 0;
		while (x.hasNext()) {

			Object p = x.next();

			int j = 0;
			for (j = 0; j < columnFields.length; j++) {
				
				columnTypes.add(columnFields[j].getType());
				
				try {
					String nameOfField = p.getClass()
							.getDeclaredField(columnFields[j].getName())
							.getName();
					nameOfField = DB4oProjUtils.INSTANCE.capitalizeString(nameOfField);
					Method m = null;
					try {
						m = p.getClass().getMethod("get" + nameOfField, null);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						dataValues[i][j] = new String(
								"(null) - no method exception.");
						continue;
					}
					Object temp = null;
					try {
						temp = m.invoke(p, null);
						if (temp == null) {
							dataValues[i][j] = new String("(null)");
						} else {
							// dataValues[i][j] = temp.toString();
							dataValues[i][j] = temp;
						}

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}

			// last column takes value of object itself for all rows
			dataValues[i][j] = p;

			i++;
		}

		JTableData jta = new JTableData(c, columnTypes,columnNames, dataValues);
		return jta;

	}
	
	public JTableData getTableDataForClassAndSuperClasses(Class<?> c) {

		
		Vector<Field> allFields = new Vector<Field>();
		Vector<Class<?>> columnTypes = new Vector<Class<?>>();
		
		Field[] classFields = c.getDeclaredFields();
		//Field[] columnFields = c.getFields();
		
		for(Field f: classFields){
//			boolean isTransient = Modifier.isTransient(f.getModifiers());
//			if(!isTransient){
//			f.setAccessible(true);
//			allFields.add(f);
//			}
			
			boolean isViewable = f.isAnnotationPresent(ViewableField.class);
			if(isViewable){
			f.setAccessible(true);
			allFields.add(f);
			}
		}
		
		
		//loop over all superclasses
		Class<?> superClass = c;
		
		while((superClass = (Class<?>) superClass.getGenericSuperclass())  != null){
		
		Field[] superClassFields = superClass.getDeclaredFields();
		
		for(Field f: superClassFields){
//			boolean isTransient = Modifier.isTransient(f.getModifiers());
//			if(!isTransient){
//			f.setAccessible(true);
//			allFields.add(f);
//			}
			
			boolean isViewable = f.isAnnotationPresent(ViewableField.class);
			if(isViewable){
			f.setAccessible(true);
			allFields.add(f);
			}
			}
		}
		
		
		String[] columnNames = new String[allFields.size() + 1];

		int z = 0;
		for (z = 0; z < allFields.size();z++) {
			columnNames[z] = WordUtils.capitalize(allFields.get(z).getName());
		}
		
		columnNames[z] = "Object Itself";

		Query q = getDb().query();
		q.constrain(c);

		ObjectSet<Class<?>> x = q.execute();

		Object[][] dataValues = new Object[x.size()][columnNames.length + 1];

		int i = 0;
		while (x.hasNext()) {

			Object p = x.next();

			int j = 0;
			for (j = 0; j < allFields.size(); j++) {
				
				try {
					
					boolean useSuperClass = false;
					
					String nameOfField = null;
					try {
						nameOfField = p.getClass().getDeclaredField(allFields.get(j).getName()).getName();
					}  catch (NoSuchFieldException e) {
						//System.out.println("not field in class, trying superclass...");
					}
					if(nameOfField == null){
						useSuperClass = true;
						try{
						nameOfField = p.getClass().getSuperclass().getDeclaredField(allFields.get(j).getName()).getName();
						}
						 catch (NoSuchFieldException e) {
							 e.printStackTrace();
							 dataValues[i][j] = new String(
										"(null) - no field exception.");
							 continue;
						}
					}
					nameOfField = DB4oProjUtils.INSTANCE.capitalizeString(nameOfField);
					Method m = null;
					try {
						if(useSuperClass){
							m = p.getClass().getSuperclass().getMethod("get" + nameOfField, null);
						}
							
						else{
							m = p.getClass().getMethod("get" + nameOfField, null);
						}
						
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						dataValues[i][j] = new String(
								"(null) - no method exception.");
						continue;
					}
					Object temp = null;
					try {
						temp = m.invoke(p, null);
						if (temp == null) {
							dataValues[i][j] = new String("(null)");
						} else {
							// dataValues[i][j] = temp.toString();
							dataValues[i][j] = temp;
						}

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}

			// last column takes value of object itself for all rows
			dataValues[i][j] = p;

			i++;
		}
		
		
		int k = 0;
		for (k = 0; k < allFields.size(); k++) {
			columnTypes.add(allFields.get(k).getType());
		}
		columnTypes.add(DB4oModel.class);
		

		JTableData jta = new JTableData(c, columnTypes, columnNames, dataValues);
		return jta;

	}


	public DB4oConnectionInfo getDbci() {
		return dbci;
	}

	public void setDbci(DB4oConnectionInfo dbci) {
		this.dbci = dbci;
	}

	public DB4oFile getDb4oFile() {
		return db4oFile;
	}

	public void setDb4oFile(DB4oFile db4oFile) {
		this.db4oFile = db4oFile;
	}

	public void openNewDb() {
		db = Db4oEmbedded.openFile(dbci.getConnectionName());
	}

	public void closeConnection() {
		if(db != null){
		db.close();
		}
		
	}

}
