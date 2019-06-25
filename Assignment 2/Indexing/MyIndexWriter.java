package Indexing;
import Classes.Path;
import java.io.*;
import java.util.*;
import java.io.File;
import java.util.Map.*;
import java.io.IOException;

public class MyIndexWriter {
	// I suggest you to write very efficient code here, otherwise, your memory cannot hold our corpus...

	private FileWriter fwDictionaryTerms;
	private BufferedWriter bwDictionaryTerms;
	
	private FileWriter fwPosting;
	private BufferedWriter bwPosting;
	
	private FileWriter fwDocId;
	private BufferedWriter bwDocId;
	
	private Map<String, int[]> dictionaryFile = new HashMap<String, int[]>();
	private Map<String, Map<Integer, Integer>> postingFile = new HashMap<String, Map<Integer, Integer>>();
	private Map<Integer, String> docIdFile = new HashMap<Integer, String>();
	
	private int count = 1;
	private int docNum = 0;
	

	String type = "";
	String path = "";
	
	private String[] fileName = {"dictionaryTerms.txt","posting.txt","docId.txt"}; 
	
	public MyIndexWriter(String type) throws IOException {
		// This constructor should initiate the FileWriter to output your index files
		// remember to close files if you finish writing the index
		this.type = type; 
		if (type == "trecweb") {
			
			path = Path.IndexWebDir;
			
		} 
		else {
			
			path = Path.IndexTextDir;
			
		}
			//write to dictionaryTerms.txt
			fwDictionaryTerms = new FileWriter(path + fileName[0]); 
			bwDictionaryTerms = new BufferedWriter(fwDictionaryTerms);
			
			//write to posting.txt
			fwPosting = new FileWriter(path + fileName[1]); 
			bwPosting = new BufferedWriter(fwPosting);
			
			//write to docId.txt
			fwDocId = new FileWriter(path + fileName[2]); 
			bwDocId = new BufferedWriter(fwDocId);
		
	}
	
	public void IndexADocument(String docno, String content) throws IOException {
		// you are strongly suggested to build the index by installments
		// you need to assign the new non-negative integer docId to each document, which will be used in MyIndexReader
		String[] termArray = new String(content).split(" ");
		int docId = Integer.parseInt(docno.substring(0, docno.indexOf("-")));
		
		String finalDocno = docno.substring(docno.indexOf("-") + 1);
		docIdFile.put(docId, finalDocno);
		

		for (String term: termArray) {
			//if term not in dictionary
			if (dictionaryFile.get(term) == null) {
				
				//create new dictionary term
				int[] place = {count, 1};					
				dictionaryFile.put(term, place); 
				
				//create new posting file
				Map<Integer, Integer> termDF = new HashMap<Integer, Integer>();
				
				termDF.put(docId, 1);
				postingFile.put(term, termDF);

				count++;
				
			} 
			else {  //if term already in dictionary
				
				//create dictionary term file
				int[] place = dictionaryFile.get(term);
				int freq = place[1] + 1;
				place[1] = freq;
				dictionaryFile.put(term, place);	
				
				//create posting file
				Map<Integer, Integer> tempFreq = new HashMap<Integer, Integer>();
				
			
				if (postingFile.get(term).get(docId) == null) {  //doesnt exist
					
					tempFreq = postingFile.get(term);
					tempFreq.put(docId, 1);
					postingFile.put(term, tempFreq);		
					
				} else {   //already exists
					
					tempFreq= postingFile.get(term);
					
					int t = postingFile.get(term).get(docId);
					t = t + 1;
					
					tempFreq.put(docId, t); 
					postingFile.put(term, tempFreq);
				}
			}
		}
		docNum++;
		if (docNum % 30000 == 0)
            createTempDict();  //creates temporary dictionary every 30000 docs
	}
	

	public void createTempDict() throws IOException {
		//create paths to temp dictionary info
		BufferedWriter tempDictionary = new BufferedWriter(new FileWriter(path + "tempDictionary" + docNum + ".txt"));
		BufferedWriter tempPosting = new BufferedWriter(new FileWriter(path + "tempPosting" + docNum + ".txt"));
		BufferedWriter tempDocId = new BufferedWriter(new FileWriter(path + "tempDocId" + docNum + ".txt"));
		
		//create temp dict
		Iterator<Entry<String, int[]>> dictIterator = dictionaryFile.entrySet().iterator(); 
		
		while (dictIterator.hasNext()) {
			Entry<String, int[]> tempTerm = dictIterator.next();
			tempDictionary.write(tempTerm.getKey().toString() + " " + tempTerm.getValue()[0] + " " + tempTerm.getValue()[1] + "\n");
		}
		//remove elements from dictionary
		dictionaryFile.clear();
		
		//create temp posting
		Iterator<Entry<String, Map<Integer, Integer>>> postIterator = postingFile.entrySet().iterator(); 
		
		while (postIterator.hasNext()) {
			Entry<String, Map<Integer, Integer>> tempPost = postIterator.next();
			Iterator<Entry<Integer, Integer>> df = tempPost.getValue().entrySet().iterator(); 
			
			tempPosting.write(tempPost.getKey() + " ");
			while (df.hasNext()) {
				Entry<Integer, Integer> tempDF = df.next();
				tempPosting.write(tempDF.getKey() + " " + tempDF.getValue() + " ");
			}
			tempPosting.write("\n");
		}
		//remove elements from posting file
		postingFile.clear();
		
		// write temp doc id
		Iterator<Entry<Integer, String>> docIdIterator = docIdFile.entrySet().iterator(); 
		while (docIdIterator.hasNext()) {
			Entry<Integer, String> tempId = docIdIterator.next();
			tempDocId.write(tempId.getKey().toString() + " " + tempId.getValue() +  "\n");
		}
		//remove elements from docId file
		docIdFile.clear();
		
		//close everything when done
		tempDictionary.close();
		tempPosting.close();
		tempDocId.close();
	}
	
	public void Close() throws IOException {
		// close the index writer, and you should output all the buffered content (if any).
		// if you write your index into several files, you need to fuse them here.
		createTempDict();
		int termNum = 0;
		
		//combine dictionary term file
		File[] directories = new File(path).listFiles();
		for(File files: directories) {
			//find tempDictionary file
			if (files.getName().contains("tempDictionary")) {
				String line = new String();

				BufferedReader br = new BufferedReader(new FileReader(files));
				
				while((line = br.readLine()) != null){
					
					String[] termArray = line.split(" ");
					
					if (dictionaryFile.get(termArray[0]) == null){ //if first term element is null
						termNum++; 
						//create first element
						int[] a = {termNum, Integer.parseInt(termArray[2])};
						dictionaryFile.put(termArray[0], a);
					}
					else { //get first element
						int[] a = dictionaryFile.get(termArray[0]);
						int freq = a[1] + Integer.parseInt(termArray[2]);
						int[] b = {a[0], freq};
						
						dictionaryFile.put(termArray[0], b);
					}
				}
				br.close();
			}
		}
		
		//create dictionaryFile.txt
		Iterator<Entry<String, int[]>> dictWriter = dictionaryFile.entrySet().iterator(); 
		while (dictWriter.hasNext()) {
			Entry<String, int[]> dictEntries = dictWriter.next();
			bwDictionaryTerms.write(dictEntries.getKey().toString() + " " + dictEntries.getValue()[0] + " " + dictEntries.getValue()[1] + "\n");
		}
		//remove everything from dictionaryFile.txt
		dictionaryFile.clear();
		
		//combine doc id file
		for(File files: directories) {
			//find tempDocId file
			if (files.getName().contains("tempDocId")) {
				String line = new String();
				
				BufferedReader br = new BufferedReader(new FileReader(files));
				
				while((line = br.readLine())!= null) {  //copy all docIds
					bwDocId.write(line);
					bwDocId.write("\n");
				}
				br.close();
			}
		}
		
		//combine posting file
		for(File files: directories) {
			if (files.getName().contains("tempPosting")) {
				String line = new String();
				
				BufferedReader br = new BufferedReader(new FileReader(files));
				
				while((line = br.readLine()) != null){  //copy all postings 
					bwPosting.write(line);
					bwPosting.write("\n");
				}
				br.close();
			}
		}
		

		//close all bufferedwriters and filewriters 
		bwDictionaryTerms.close();
		bwPosting.close();
		bwDocId.close();
		fwDictionaryTerms.close();
		fwPosting.close();
		fwDocId.close();
	}
	
}

