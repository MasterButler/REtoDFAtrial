package filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TextFileReader{
	
	public static String read(String location) {
		File file = new File(location);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			  
			String st; 
			while ((st = br.readLine()) != null) 
			    
			return st;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
