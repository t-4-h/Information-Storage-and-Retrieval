package Search;

/**
 * This is for INFSCI 2140 in 2019
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	// Essential private methods or variables can be added.
	private char[] texts;
    private int pos = 0;  //to keep track of position
    
	public WordTokenizer( char[] texts ) {
		// Tokenize the input texts.
        this.texts=new char[texts.length];
        for(int i = 0; i < this.texts.length; i ++){
            this.texts[i] = texts[i];
        }
	}
	
	// YOU MUST IMPLEMENT THIS METHOD.
    public char[] nextWord() {
    	// Return the next word in the document.
    	// Return null, if it is the end of the document.
        int i = pos;
        char[] word;

       //first loop to exclude spaces & punctuation
        for(;i<texts.length-1;i++){
            if(!Character.isLetterOrDigit(texts[i])) {
                pos++;  //move position
                continue;
            }
            break;
        } 
        
        for(pos=i;i<texts.length-1;i++) {
            if(texts[i]==' ' || (!Character.isLetterOrDigit(texts[i]) && texts [i+1] == ' ') ||
              (i==texts.length-1 && i!=pos)){
                word = new char[i - pos];
                for(int j=0;j<i-pos;j++)
                    word[j]=texts[pos+j];
                pos = i;   //update position
                return word;
            }
        }
        return null;
    }
	
}