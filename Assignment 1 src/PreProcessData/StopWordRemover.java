package PreProcessData;
import Classes.*;
import java.io.*;
import java.util.*;
import java.io.File;


public class StopWordRemover {
	// Essential private methods or variables can be added.
    private BufferedReader br;
    private String line = null;  
    private Set<String> words = new HashSet<String>();
    
    
    // YOU SHOULD IMPLEMENT THIS METHOD. (WORKING)
	public StopWordRemover( ) throws IOException {
		// Load and store the stop words from the fileinputstream with appropriate data structure.
		// NT: address of stopword.txt is Path.StopwordDir
        File file = new File(Path.StopwordDir);
        this.br = new BufferedReader(new FileReader(file));
        //read through stopword file & add words until empty
        while ((line = br.readLine()) != null){  
            words.add(line);
        }
	}

	// YOU SHOULD IMPLEMENT THIS METHOD. (WORKING)
	public boolean isStopword( char[] word ) {
		// Return true if the input word is a stopword, or false if not.
        if(words.contains(new String(word))) {
            return true;
        }
        else
            return false;
	}
}