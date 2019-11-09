
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecompressLZW {
	
	// ----object fields-------
	String filename;
	byte[] file;
	Map<Integer, String> dictionary;
	int dictionarysize;	
	List<Integer> codes;
	StringBuilder decompressed;	
	// -----------------------
	
	
	//constructor
	public DecompressLZW(String filename) throws Exception{
		
		this.filename = filename;
		
		// initialise dictionary and fields for given instance object
		dictionary = new HashMap<>();
		prepareDictionary();
		
		codes = new ArrayList<Integer>();
		decompressed = new StringBuilder();
		
		// convert File into bytes array (can throw Exception)
		file = Files.readAllBytes(new File(filename).toPath());
	}
	
	
	
	// prepare dictionary with ASCIIs (0 to 255)
	private void prepareDictionary(){

		for(int i=0; i < 256; i++){
			dictionary.put(i, String.valueOf((char)i));					
		}
		
		dictionarysize = 256; // dictionary will now have 256 entries
	}
	
	
	
	// fill in List<Integer> codes (gets 3 bytes each time, converts into two 12-bit codes, stores in List)
	private void prepareCodes(){
		
		int excess = file.length % 3; // if file length is not multiple of 3
		
		int mask = 15; // 0000 1111, to only obtain the 4 bits on the right
		int code1, code2;
		
		// get File bytes (3 at a time) and store them as Integer codes in List
		for(int i = 0; i < file.length-excess; i += 3){
			
			int a = Byte.toUnsignedInt(file[i]);
			int b = Byte.toUnsignedInt(file[i + 1]);
			int c = Byte.toUnsignedInt(file[i + 2]);
			
			// generate codes
			code1 =  a << 4;
			code1 =  code1 | (b >> 4 & mask);

			code2 = (mask & b) << 8;
			code2 = code2 | c;
			
			// add codes to List
			codes.add(code1);
			codes.add(code2);
		}
		
	
		// pack last pair if length of File is not a multiple of 3
		if(excess!=0){
			int a = Byte.toUnsignedInt(file[file.length-1]);
			int b = Byte.toUnsignedInt(file[file.length-2]);

			code1 = (b << 8) | a;
			codes.add(code1);
		}
	}
	

	// decompress file using dictionary and codes
	public void decompress(){
		
		// prepare codes for given object
		prepareCodes();

		String previous = "" + (char)(int)codes.remove(0);
		decompressed.append(previous);
		
		for (Integer i: codes) {
			
	        String entry = "";
	        
	        if (dictionary.containsKey(i))
	            entry = dictionary.get(i);
	        else
	        	entry = previous + previous.charAt(0); // if i==dictionarysize
     
	        // add code to dictionary
	        dictionary.put(dictionarysize++, previous + entry.charAt(0));
	        
	        // this is now decompressed text
	        decompressed.append(entry);
	        previous = entry;
	        
	        // reset the dictionary at 4096 entries
			if(dictionarysize == 4096){
				dictionary = new HashMap<>();
				prepareDictionary();
			}
	    }
	}
	
		

	// create a .txt file with the decompressed content
	public void outputDecompressedFile() throws IOException {

		// e.g. "compressedfile1.z" ---> "Decompressed - compressedfile1.txt"
		String savename = "Decompressed - " + this.filename.substring(0, this.filename.length()-2) + ".txt";	
		File outFile = new File(savename);

		FileWriter myFW = new FileWriter(outFile);
		PrintWriter myfile = new PrintWriter(myFW);

		myfile.print(this.decompressed);
		myfile.close();
	}
	
}


