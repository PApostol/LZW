
public class Main {	
	
	// main method for decompressing
	public static void main (String[] args){
		
		try {
		
		// initialise
		DecompressLZW file1 = new DecompressLZW("compressedfile1.z");
		DecompressLZW file2 = new DecompressLZW("compressedfile2.z");
		DecompressLZW file3 = new DecompressLZW("compressedfile3.z");
		DecompressLZW file4 = new DecompressLZW("compressedfile4.z");
		
		// decompress
		file1.decompress();
		file2.decompress();
		file3.decompress();
		file4.decompress();
			
		// save as .txt
		file1.outputDecompressedFile();
		file2.outputDecompressedFile();
		file3.outputDecompressedFile();
		file4.outputDecompressedFile();
		
		System.out.println("Done.");
		
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
