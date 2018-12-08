import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

import filereader.TextFileReader;


public class NoisyPhoneNumberGenerator {
	
	public static String FILENAME = "noisy phone number listing.txt";
	
	public static String generate() {
		String content = TextFileReader.read(FILENAME);		
		if(content == null) {
			System.out.println("Creating new file for storing phone number");
			content = generateNoisyPhoneNumber();
		}else {
			System.out.println("File is already existing. Using said file instead.");
		}
		return content;
	}
	
	private static String generateNoisyPhoneNumber() {
		String fileContent = generateNoisyPhoneNumberString();
		PrintWriter out;
		
		try {
			out = new PrintWriter(FILENAME);
			out.print(fileContent);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileContent;
	}
	
	private static String generateNoisyPhoneNumberString() {
		String inputString = "";
		Random rand = new Random(System.currentTimeMillis());
		int ctr = 0;
		while(inputString.length() < 100) {
			int generated = rand.nextInt(101);
			if(generated <= 80) {
				inputString += rand.nextInt(10);
			}else{
				if(generated <= 90) {
					inputString += "+63";
				}else {
					inputString += "0";					
				}
				inputString+=9;
				for(int i = 0; i < 9; i++) {
					inputString+=rand.nextInt(10);
				}
				ctr++;
			}
			
		}
		System.out.println("Expected input count: at least:" + ctr);
		return inputString;
	}
	
}
