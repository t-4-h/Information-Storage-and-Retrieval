package PseudoRFSearch;

import java.util.*;
import java.io.IOException;



import Classes.Document;
import Classes.Query;
import IndexingLucene.MyIndexReader;


public class PseudoRFRetrievalModel {
	
	private int miu;
	private int length;
   
	
    MyIndexReader indexReader;
    
	public PseudoRFRetrievalModel(MyIndexReader ixreader) throws IOException
	{
		
		this.indexReader=ixreader; 
		
		miu = 2000; 
		 
		 
		//get collection length
		length = indexReader.collectionLen();
       
	}
	
	/**
	 * Search for the topic with pseudo relevance feedback in 2017 spring assignment 4. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<Document> RetrieveQuery( Query aQuery, int TopN, int TopK, double alpha) throws Exception {	
		// this method will return the retrieval result of the given Query, and this result is enhanced with pseudo relevance feedback
		// (1) you should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents
		// (2) implement GetTokenRFScore to get each query token's P(token|feedback model) in feedback documents
		// (3) implement the relevance feedback model for each token: combine the each query token's original retrieval score P(token|document) with its score in feedback documents P(token|feedback model)
		// (4) for each document, use the query likelihood language model to get the whole query's new score, P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
		String[] array = aQuery.GetQueryContent().split(" ");
		
        HashMap<String, Long> cf = new HashMap<>(); //collection frequencies (cf)
        
        List<Document> results = new ArrayList<Document>();
        List<Document> initRank = new ArrayList<Document>(); 
        
        Map<Integer, HashMap<String, Integer>> documentId = new HashMap<Integer, HashMap<String, Integer>>();  //map for doc ids
        
        //find df for each term in query and insert into map
         for (String term : array) {
        	 
            int[][] posting = indexReader.getPostingList(term);		
            cf.put(term, indexReader.CollectionFreq(term));	
            
            //if any entries in posting list exist, loop thru list
            if (posting != null) {		
            	
                for (int[] termFreq : posting) {
                	
                    int docid = termFreq[0];
                    int tf = termFreq[1]; 
                    
                    //if current doc id already exists in map update tf and add 
                    if (documentId.containsKey(docid)) {
                    	
                    	
                        HashMap<String, Integer> frequencies = documentId.get(docid);
                        

                    	int prevFreq = 0;
                        //if term already exists in frequency map get previous freq 
                        if (frequencies.containsKey(term)) {
                        	
                                prevFreq = frequencies.get(term);
                                
                        }
                        
                        //calculate current frequency then update maps
                        int currentFreq = tf + prevFreq; 
                        
                        frequencies.put(term, currentFreq); //freq map
                        documentId.put(docid, frequencies); //docid map
                    } 
                    
                    //when doc id doesnt already exist
                    else {								
                    	
                    	//create new entry (dont need to update freq first)
                        HashMap<String, Integer> frequencies = new HashMap<>();
                        
                        frequencies.put(term, tf); //freq map
                        documentId.put(docid, frequencies); //docid map
                    }
                }
    }
  }
      
         //calculate scores for every doc entry in map
         
         for (Map.Entry<Integer, HashMap<String, Integer>> entry : documentId.entrySet()) {
       
            HashMap<String, Integer> frequencies = entry.getValue();
            double score = 1;
            
            //get doc id and length
            int id = entry.getKey();
            int docLength = indexReader.docLength(id);
            
            
            for (String term : array) {			
            	
                long currentCf = cf.get(term);
                
                //if already exists in collection
                if (currentCf != 0) {			
                    int docFreq = 0;
                 
                    if (frequencies.containsKey(term)) {	
                    	
                  
                            docFreq = frequencies.get(term);
                            
                    }
                	//use dirichlet smoothing
                    score = 
                    score * ((docFreq + miu *( (double)currentCf / length) ) / 
                    		(docLength + miu));
                }
            }
            //create new doc and add to initial list
            Document doc = new Document(Integer.toString(id), indexReader.getDocno(id), score);
            initRank.add(doc);									
        }
        
         //sort list to get top k
        Collections.sort(initRank, Collections.reverseOrder(new Comparator<Document>() { 	
            public int compare(Document a, Document b) {
            	
                return Double.compare(a.score(), b.score());
                
            }
        }));
		
		// get P(token|feedback documents)
        
		HashMap<String,Double> TokenRFScore=GetTokenRFScore(aQuery,TopK, initRank);
		
		// sort all retrieved documents from most relevant to least, and return TopN
		for (Map.Entry<Integer, HashMap<String, Integer>> entry : documentId.entrySet()) {
			
            HashMap<String, Integer> frequencies = entry.getValue();
            double score = 1;
            
            
            int finalDocId = entry.getKey();
            int finalDocLen = indexReader.docLength(finalDocId);
            
			//get feedback and calculate final scores
            
            for (String term : array) {		
            	
            		long finalCf = cf.get(term);
            		
            		//feedback score
            		double fbScore = TokenRFScore.get(term);		
            	
                
                if (finalCf!= 0) {		
                	
                    int freq = 0;
                    
                    if (frequencies.containsKey(term)) {
                    	
                    		freq = frequencies.get(term);
                    		
                    }
                    //previous score
                    double prevScore = (freq + miu * ((double)finalCf/length)) 
                    		/ (finalDocLen + miu);
                    
                    
                    //updated score
                    score = score * (alpha * prevScore + (1-alpha) * fbScore);
                }
            }
            
            //final document list
            Document doc = new Document(Integer.toString(finalDocId), indexReader.getDocno(finalDocId), score);	
            results.add(doc);									
        }
		
		//sort document list to get top n 
        Collections.sort(results, Collections.reverseOrder(new Comparator<Document>() {
            public int compare(Document a, Document b) {
            	
                return Double.compare(a.score(), b.score());
                
            }
        }));
		//return top n
		return results.subList(0, TopN);
	}
	
	
	
		public HashMap<String,Double> GetTokenRFScore(Query aQuery,  int TopK, List<Document> initRank) throws Exception {
		// for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing
		// save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String,Double> TokenRFScore=new HashMap<String,Double>();
		
		
		String[] queries = aQuery.GetQueryContent().split(" ");
		
		ArrayList<Integer> topKDocId = new ArrayList<Integer>();
		
		List<Document> topK = new ArrayList<Document>();

		
		Map<Integer, HashMap<String, Integer>> documentId = new HashMap<Integer, HashMap<String, Integer>>();
		HashMap<String, Long> cf = new HashMap<>();
		
		//get top k
		topK = initRank.subList(0, TopK);
		
		for (Document docu: topK) {
			
			topKDocId.add(Integer.parseInt(docu.docid()));
			
		}
		
		//get doc id and freq from posting list
		for (String term : queries) {	
			
            int[][] posting = indexReader.getPostingList(term);
            cf.put(term, indexReader.CollectionFreq(term));
            
            if (posting != null) {
            	
                for (int[] termFreq : posting) {
                	
                    int docid = termFreq[0];
                    int frequency = termFreq[1];
                    
                    if (documentId.containsKey(docid)) {	
                    
                        HashMap<String, Integer> frequencies = documentId.get(docid);
                        int prevFreq = 0;
                        //update frequency if already exists
                        if (frequencies.containsKey(term)) {
                        	
                                prevFreq = frequencies.get(term);
                                
                        }
                        
                        int currentFreq = frequency + prevFreq; 
                        
                        frequencies.put(term, currentFreq);
                        documentId.put(docid, frequencies);
                    } 
                    //if doesnt exist create new without updating frequency
                    else {
                        HashMap<String, Integer> frequencies = new HashMap<>();
                        frequencies.put(term, frequency);
                        documentId.put(docid, frequencies);
                    }
                }
            }
        }
		
		//find feedback score
		for (String term: queries) {									
			
			int docLen = 0;
			int termDf = 0;
			
			long fbCf = cf.get(term);	
			
			double score = 1.0;
			
			for (int id: topKDocId) {
				//get doc length and term freq of top k
				docLen += indexReader.docLength(id);				
				HashMap<String, Integer> frequencies = documentId.get(id);
				
				if (frequencies.get(term) != null) {
					
					termDf += frequencies.get(term);
					
				}
			}
			
			//calculate feedback score using dirichlet smoothing
			score = (termDf + miu*(fbCf/length)) / (docLen + miu);
			
			//enter scores
			TokenRFScore.put(term, score);
		}
	
		return TokenRFScore;
	}
}