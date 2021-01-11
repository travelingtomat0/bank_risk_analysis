import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseModule {
	
	/*
	 * Format of the CSV-file:
	 * Date,Open,High,Low,Close,Adj Close,Volume
	 * Parsed to file that leaves out adj. close and Volume
	 */
	
	ArrayList<String[]> instantiateFile(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		String line = "";
        String cvsSplitBy = ",";
        ArrayList<String[]> list = new ArrayList<String[]>();
        
        sc.nextLine();
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			list.add(line.split(cvsSplitBy));
		}
		
		sc.close();
		return list;
	}
	
}