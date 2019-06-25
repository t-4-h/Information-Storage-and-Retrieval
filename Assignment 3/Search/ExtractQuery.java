
package Search;

import java.io.BufferedReader;
import java.io.FileReader;
import Classes.Query;
import java.io.IOException;

public class ExtractQuery {
	String line;
	BufferedReader br = new BufferedReader(new FileReader("data/topics.txt"));
	
	public ExtractQuery() throws IOException {
		//you should extract the 4 queries from the Path.TopicDir
		//NT: the query content of each topic should be 1) tokenized, 2) to lowercase, 3) remove stop words, 4) stemming
		//NT: you can simply pick up title only for query, or you can also use title + description + narrative for the query content.
		line = "";
	}
	
	public boolean hasNext() throws IOException{
		
		if ((line = br.readLine()) == null) {
			return false;
		} 
		else {
			return true;
		}
	}
	
	public Query next() throws IOException{
		Query q;
		String content = "";
		String id = "";
		String title = "";
	
		//for each document 
		while (!line.contains("</top>")) {
			//get id number
			if (line.contains("<num>")) {	
				int docid = line.indexOf(":") + 1;
				id = line.substring(docid);
			}
			//get title
			if (line.contains("<title>")){	
				int doctitle = line.indexOf("> ") + 2;
				title = line.substring(doctitle);
			}
			
			line = br.readLine();
		}
		
		content = title; 
		char[] contentArray = content.toCharArray();
		content = "";
		char[] word = null;
		
		WordNormalizer normalizer = new WordNormalizer();
		WordTokenizer tokenizer = new WordTokenizer(contentArray);
		StopWordRemover stopword = new StopWordRemover();

		//change to lowercase, normalize, stem (unless stopword)
		while ((word = tokenizer.nextWord()) != null) {
			word = normalizer.lowercase(word);

			if (!stopword.isStopword(word)) {
				content += normalizer.stem(word) + " ";
			}
		}
		
		q  = new Query(content, id);
		return q;
	}
}






