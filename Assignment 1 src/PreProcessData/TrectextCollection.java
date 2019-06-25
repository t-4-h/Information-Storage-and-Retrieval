package PreProcessData;
import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import Classes.Path;

/**
 * This is for INFSCI 2140 in 2019
 *
 */
public class TrectextCollection implements DocumentCollection {
	// Essential private methods or variables can be added.
	private BufferedReader br;
    private final static String pattern_id = "<DOCNO>(.+)</DOCNO>";
    public Map<String, Object> map = new HashMap();
    
    
	// YOU SHOULD IMPLEMENT THIS METHOD. (WORKING)
	public TrectextCollection() throws IOException {
		// 1. Open the file in Path.DataTextDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!
		File file = new File(Path.DataTextDir);
        this.br = new BufferedReader(new FileReader(file));
	}
	
	// YOU SHOULD IMPLEMENT THIS METHOD. (WORKING)
	public Map<String, Object> nextDocument() throws IOException {
		// 1. When called, this API processes one document from corpus, and returns its doc number and content.
		// 2. When no document left, return null, and close the file.
		String content = null;
        String line = null;
        String id = null;
      
        
        while ((line = br.readLine()) != null){
            if (line.equals("<DOC>")){
                line = br.readLine();
           
                //create pattern and match object
                Pattern p = Pattern.compile(pattern_id);
                Matcher m = p.matcher(line);
                if (m.find()){
                    id = m.group(1);
                }
                
                while(!(line=br.readLine()).equals("<TEXT>"));
                StringBuilder stringbuilder = new StringBuilder(); //init stringbuilder
                
                //get content
                while(line != null && !(line=br.readLine()).equals("</TEXT>")){
                    stringbuilder.append(line).append(" ");
                }
                content = stringbuilder.toString();

                //convert to char array & store to hashmap
                char[] array = content.toCharArray();
                map.clear();
                map.put(id, array);
                return map;
            }
        }
        br.close();
		return null;
	}
	
}