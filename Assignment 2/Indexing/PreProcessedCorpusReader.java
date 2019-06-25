package Indexing;
import Classes.*;
import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;


public class PreProcessedCorpusReader {
	
	String type = "";
	private int docId = 0;
	private FileReader fr;
	private BufferedReader br;


	public PreProcessedCorpusReader(String type) throws IOException {
		// This constructor opens the pre-processed corpus file, Path.ResultHM1 + type
		// You can use your own version, or download from http://crystal.exp.sis.pitt.edu:8080/iris/resource.jsp
		// Close the file when you do not use it any more
		fr = new FileReader(Path.ResultHM1 + type); 
		br = new BufferedReader(fr);
		this.type = type;  //update type
	}
	

	public Map<String, String> NextDocument() throws IOException {
		// read a line for docNo, put into the map with <"DOCNO", docNo>
		// read another line for the content , put into the map with <"CONTENT", content>
		Map<String, String> map = new HashMap<>();
		String docNo = new String();
		String line = new String();
		String content;
		
		//set docNo and close when done
		if ((line = br.readLine())==null) {
			fr.close();
			br.close();
			return null;
		} else
			docId ++;  //update docID
			docNo = docId +"-"+ line;
			
			map.put("DOCNO", docNo);
		
		//set content and close when done
		if ((line = br.readLine())==null) {
			fr.close();
			br.close();
			return null;
		} else
			content = line.toString();
			map.put("CONTENT", content);
		
		return map;
	}

}
