package filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TextFileReader{
	
	public static String read(String location) {
		File file = new File(location);
		if(file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file)); 
				
				String toRet = "";
				String st; 
				while ((st = br.readLine()) != null) {
					toRet += st + "\n";
				}
				return toRet;
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}else {
			System.out.println("File does NOT exist.");
		}
		return null;
	}
	
}
