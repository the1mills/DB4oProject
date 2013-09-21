package staticClasses;

public enum DB4oProjUtils {
	
	INSTANCE;
	
	public static String capitalizeString(String string) {
		  char[] chars = string.toCharArray();
		 
		   Character firstLetter = Character.toUpperCase(chars[0]);
		 
		   chars[0] = firstLetter; 
		  
		  return String.valueOf(chars);
	}

}
