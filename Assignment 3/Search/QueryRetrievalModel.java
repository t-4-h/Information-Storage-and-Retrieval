package Search;

import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.io.IOException;
import Classes.*;
import IndexingLucene.*;


public class QueryRetrievalModel {
	
	protected MyIndexReader indexReader;
	private int collectionLength;
	int miu; 
	
	public QueryRetrievalModel(MyIndexReader ixreader) throws IOException {

		indexReader = ixreader;
		collectionLength = indexReader.collectionLen();
		
		miu = 2000;		//set min info unit = 2000
		
	}
	
	/**
	 * Search for the topic information. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * TopN specifies the maximum number of results to be returned.
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @return
	 */
	
	public List<Document> retrieveQuery( Query aQuery, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the documents based on their relevance score, from high to low
		Map<Integer, Double> scoreMap = new HashMap<Integer, Double>();
		Map<Integer, Map<String, Integer>> docIdMap = new HashMap<Integer, Map<String, Integer>>();
		
		String[] qArray = aQuery.GetQueryContent().split(" ");
		
		List<Document> list = new ArrayList<Document>();

		for(String findWord: qArray) {
			if (indexReader.CollectionFreq(findWord)== 0)	// if not in collection, continue
				continue;
			
			int[][] postingList = indexReader.getPostingList(findWord);
			
			for (int[]a: postingList) {
				//set doc id and word frequency
				int id = a[0];		
				int freq = a[1];		
				
				//if does not exist in map
				if (docIdMap.get(id) == null) {
					Map<String, Integer> temp = new HashMap<String, Integer>();
					//add to map
					temp.put(findWord, freq);
					docIdMap.put(id, temp);
				} 
				else {		
					//if already exists in map
					Map<String, Integer> temp = new HashMap<String, Integer>();
					//edit and add to map
					temp = docIdMap.get(id);
					temp.put(findWord, freq);
					docIdMap.put(id, temp);
				}
			}
			postingList = new int[][] {};
		}
		
		//iterate through the docid map and calculate score of each docid
		for (int docId: docIdMap.keySet()) {	
			double score = 1.0;
			Map<String, Integer> temp = new HashMap<String, Integer>();
			temp = docIdMap.get(docId);
			
			//iterate through query to calculate score
			for(String word: qArray) {		
				
				//continue if query word not in collection
				if (indexReader.CollectionFreq(word) == 0)
					continue;
				
				int count;
				if (temp.get(word) == null) {
					count = 0;
				}
				else {
					count = temp.get(word);
				}
				
				double p = indexReader.CollectionFreq(word)/collectionLength;
				int length = indexReader.docLength(docId);
				
				//use Dirichlet smoothing
				double wordScore =  (count + miu*p) / (length + miu);	
				
				//use Bayes formula
				score = score*wordScore;		
			}
			scoreMap.put(docId, score);
		}
		
		List<Entry<Integer, Double>> greatest = findGreatest(scoreMap, TopN);	
		
		ArrayList<Integer> idArray = new ArrayList<Integer>();
		ArrayList<Double> scoreArray = new ArrayList<Double>();
		
		//put topN keys and values into arrayList
		for (Entry<Integer, Double> entry : greatest) {	
			idArray.add(entry.getKey());		
            scoreArray.add(entry.getValue());
        }
		
		//create document containing topN docs
		for (int i = idArray.size()-1; i >= 0; i--) {		
			String docno = indexReader.getDocno(idArray.get(i));
			list.add(new Document(idArray.get(i)+"", docno, scoreArray.get(i)));
		}
		System.out.println("The Document list size is: " + list.size());
		
		return list;
	}
		
	
	//priority tree to find topN value
	private <K, V extends Comparable<? super V>> List<Entry<K, V>> findGreatest(Map<K, V> map, int n){
	    Comparator<? super Entry<K, V>> comparator = new Comparator<Entry<K, V>>() {
	        @Override
	        public int compare(Entry<K, V> e0, Entry<K, V> e1) {
	            V v0 = e0.getValue();
	            V v1 = e1.getValue();
	            return v0.compareTo(v1);
	        }
	    };
	    PriorityQueue<Entry<K, V>> highest = new PriorityQueue<Entry<K,V>>(n, comparator);
	    for (Entry<K, V> entry : map.entrySet()) {
	        highest.offer(entry);
	        while (highest.size() > n) {
	            highest.poll();
	        }
	    }
	    List<Entry<K, V>> result = new ArrayList<Map.Entry<K,V>>();
	    while (highest.size() > 0) {
	        result.add(highest.poll());
	    }
	    return result;
	}
	
}