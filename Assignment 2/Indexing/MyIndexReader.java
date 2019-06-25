package Indexing;
import Classes.*;
import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.io.IOException;



public class MyIndexReader {
	//you are suggested to write very efficient code here, otherwise, your memory cannot hold our corpus...
	private FileReader frDictionary;
	private BufferedReader brDictionary;
	
	private FileReader frPosting;
	private BufferedReader brPosting;

	private FileReader frDocId;
	private BufferedReader brDocId;

	Map<String, int[]> dictionaryTerms;
	Map<String, Map<Integer, Integer>> tokenPosting = new HashMap<String, Map<Integer, Integer>>();
	Map<Integer, String> docId;
	
	private String path = "";
	
	String line = "";
	String type = "";
	int pointer = 0;

	
	public MyIndexReader( String type ) throws IOException {
		//read the index files you generated in task 1
		//remember to close them when you finish using them
		//use appropriate structure to store your index
		this.type = type;
		
		if (type == "trecweb") {
			
			path = Path.IndexWebDir;
			
		} 
		else {
			
			path = Path.IndexTextDir;
			
		}	
		
			frDictionary = new FileReader(path + "dictionaryTerms.txt"); 
			brDictionary = new BufferedReader(frDictionary);
			
			frPosting = new FileReader(path + "posting.txt"); 
			brPosting = new BufferedReader(frPosting);
			
			frDocId = new FileReader(path + "docId.txt"); 
			brDocId = new BufferedReader(frDocId);
			
			dictionaryTerms = new HashMap<String, int[]>();
			//get elements from dictionaryFile.txt
			while ((line = brDictionary.readLine()) != null) {
				
				String [] termArray = line.split(" ");
				int[] temp = {Integer.parseInt(termArray[1]), Integer.parseInt(termArray[2])};
				
				dictionaryTerms.put(termArray[0], temp);
				
			}
		
			docId = new HashMap<Integer, String>();
			//get elements from docId.txt
			while ((line = brDocId.readLine()) != null) {
				
				String [] idArray = line.split(" ");
				docId.put(Integer.parseInt(idArray[0]), idArray[1]);
				
			}
		
	}
		
	
	//get the non-negative integer dociId for the requested docNo
	//If the requested docno does not exist in the index, return -1
	public int GetDocid( String docno ) {
		int result = -1;
		//iterate through docIds to find docId for docno
		Iterator<Entry<Integer, String>> docIterator = docId.entrySet().iterator(); 
		
		while (docIterator.hasNext()) {
			
			Entry<Integer, String> id = docIterator.next();
			
			if(id.getValue().equals(docno)) {
				
				result = id.getKey();
			}
		}
		return result;  //result will be -1 if docno doesn't exist in the index
	}

	// Retrieve the docno for the integer docid
	public String GetDocno( int docid ) {
		
		return docId.get(docid);
		
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] GetPostingList( String token ) throws IOException {
		
		int col = tokenPosting.get(token).size();		
		int[][] result = new int[col][2];
		ArrayList<Integer> postingArray = new ArrayList<Integer>();
		
		Iterator<Entry<Integer, Integer>> postingIterator = tokenPosting.get(token).entrySet().iterator(); 
		//iterate through the token posting map and create 2D array
		while (postingIterator.hasNext()) {
			
			Entry<Integer, Integer> postings = postingIterator.next();
			postingArray.add(postings.getKey());
			postingArray.add(postings.getValue());
			
		}
		
		for (int j = 0; j < result.length; j++) {
			
			result[j][0] = postingArray.get(2*j);
			result[j][1] = postingArray.get(j*2+1);
			
		}
		
		return result;
	}
	
	
	// Return the number of documents that contains the token.
	public int GetDocFreq( String token ) throws IOException {
		
		GetPosting(token);
		
		//return 0 if no documents contain the token
		if (dictionaryTerms.get(token)==null) {
			
			return 0;
			
		}
		else {
			
		return tokenPosting.get(token).size();
		
		}
	}
	
	// Return the total number of times the token appears in the collection.
	public long GetCollectionFreq( String token ) throws IOException {
		
		//return 0 if token doesn't appear in collection
		if (dictionaryTerms.get(token)==null) {

			return 0;
			
		} else {
			
			int[] freqArray = dictionaryTerms.get(token);
			return freqArray[1];
			
		}
	}

	
	public void GetPosting (String token) throws IOException {
			
		String line = "";
		
		Map<Integer, Integer> freqMap = new HashMap<Integer, Integer>();
		
	
		while ((line = brPosting.readLine()) != null){
			
			String[] postArray = line.split(" ");
			
			//if first element in tokenPosting is equal to token
			if (postArray[0].equals(token)) {
					for (int i = 1; i < postArray.length; i++) {
							if (i%2 == 1) {
							freqMap.put(Integer.parseInt(postArray[i]), Integer.parseInt(postArray[i+1]));
							}
						
					}
					//if token doesnt exist in tokenPosting map
					if (tokenPosting.get(token) == null){
						
						tokenPosting.put(postArray[0], freqMap);
					}
					//if token exists in tokenPosting map
					else {
						Map<Integer, Integer> tempTokenArray = new HashMap<Integer, Integer>();
						tempTokenArray = tokenPosting.get(token);
						//put all elements into freqMap
						tempTokenArray.putAll(freqMap);
						tokenPosting.put(postArray[0], tempTokenArray);
					} 
				}
			}
		}
		
	//close all bufferedreaders and filereaders
		public void Close() throws IOException {
			brDictionary.close();
			brPosting.close();
			brDocId.close();
			frDictionary.close();
			frPosting.close();
			frDocId.close();
	}
	
}