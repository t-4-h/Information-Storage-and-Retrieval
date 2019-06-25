Java version: 10.0.2
Built in Eclipse IDE

Everything is located in the src folder.
Inside the src folder should be two additional folders named “Indexing” and “Classes” which hold all the scripts necessary to run the program. 

To Run: add a new folder to the src folder called “data”. 
In this folder should be "result.trectext" and "result.trecweb" containing the results from HW1.
You will also need to add two folders inside the data folder: one folder called “indextext” 
and one folder called “indexweb” to save the txt files created when program runs. 
Then you can compile & run the program from HW2Main.java


When the program runs, it will create temporary files inside indexweb and indextemp before merging them into 3 files named
“dictionaryTerms.txt” “docId.txt” and “posting.txt” inside each folder.  All temporary file names start with “temp”.

The runtime is slightly different each time I run the program but is 5-6.5 minutes total on average. 


AVERAGE RESULTS: 

(EXAMPLE 1)
totaly document count:  198361
index web corpus running time: 1.5056166666666666 min
 >> the token "acow" appeared in 3 documents and 3 times in total
       lists-092-3952951    154963         1
      lists-108-11347927    186006         1
       lists-092-4113429    154964         1
load index & retrieve running time: 0.10038333333333334 min

totaly document count:  503473
index text corpus running time: 4.215483333333333 min
 >> the token "yhoo" appeared in 5 documents and 5 times in total
        NYT19990208.0397    291085         1
        NYT20000717.0201    477373         1
        NYT20000927.0406    502701         1
        NYT19990405.0253    313384         1
        NYT20000928.0343    503146         1
load index & retrieve running time: 0.3336166666666667 min
(total runtime approximately 6.1 min)

(EXAMPLE 2)
totaly document count:  198361
index web corpus running time: 1.3517 min
 >> the token "acow" appeared in 3 documents and 3 times in total
       lists-092-3952951    154963         1
      lists-108-11347927    186006         1
       lists-092-4113429    154964         1
load index & retrieve running time: 0.08505 min

totaly document count:  503473
index text corpus running time: 3.8181833333333333 min
 >> the token "yhoo" appeared in 5 documents and 5 times in total
        NYT19990208.0397    291085         1
        NYT20000717.0201    477373         1
        NYT20000927.0406    502701         1
        NYT19990405.0253    313384         1
        NYT20000928.0343    503146         1
load index & retrieve running time: 0.39865 min
(total runtime approximately 5.5 mins)

SLOWEST RESULTS:
totaly document count:  198361
index web corpus running time: 1.64775 min
 >> the token "acow" appeared in 3 documents and 3 times in total
       lists-092-3952951    154963         1
      lists-108-11347927    186006         1
       lists-092-4113429    154964         1
load index & retrieve running time: 0.17816666666666667 min

totaly document count:  503473
index text corpus running time: 6.856883333333333 min
 >> the token "yhoo" appeared in 5 documents and 5 times in total
        NYT19990208.0397    291085         1
        NYT20000717.0201    477373         1
        NYT20000927.0406    502701         1
        NYT19990405.0253    313384         1
        NYT20000928.0343    503146         1
load index & retrieve running time: 0.5745333333333333 min

-Total runtime approximately 9.1 minutes








Tasks
Task 1: Build an index.
In this task, you should implement:
•	Indexing. PreProcessedCorpusReader
You will need to get access to the result.trectext and result.trecweb, and return document one by one through the nextDocument(). Reference HW1Main class in assignment 1 for the format of the document. If you do not want to use your own result.trectext and result.trecweb, you can DOWNLOAD the versions we provide to you from：

https://drive.google.com/file/d/1xr2O-qbq-uRe8XZuYpBrc54JNIGdLfo9/view?usp=sharing 

•	Indexing.MyIndexWriter
This class has one essential method IndexADocument (String docno, String content) to create index for a document represented by the docno and the content. The content is a list of words, segmented by blank space generated in the Assignment 1. You will need to write very efficient code in this class, otherwise your memory may fail to support your code.  If your computer have a memory smaller than 8G, we strongly suggest you to construct the index by installments, where each installment works on only a block of the documents to be indexed. For example, each block can have n document (n can be 10000, 20000, etc). When processing the documents in a block, everything about the index can be stored in the memory, then when all the documents in the block is processed, the corresponding dictionary and postings can be stored as separate files on the hard drive so that the memory is cleaned for the next block of documents. Once all the blocks have been processed, there will be a fusion process to merge all the dictionary files, and all the posting files. If you are not clear on this process, consult the part of the slides. In this way, small memory computer can process big corpus.
You need to develop your own data structures used for dictionary term file and posting files. But we strongly suggest you first look at task 2 before implementing task 1, so that you can check whether your index can support search tasks in task 2.
Task 2: Retrieve posting lists of tokens from an index 
In this task, you should implement:
•	Indexing. MyIndexReader, which has the following methods:
o	MyIndexReader(): read the index file you generated in task 1. Do not load the whole index into the memory. A proper implementation should load the dictionary term file first. Once the corresponding links of the posting information of the query terms are known, you can load the relevant parts of the postings into the memory.
o	int GetDocid( String docno ) and String getDocno( int docid ): provides transformation between string docnos and integer docids.
o	int[][] GetPostingList( String token ): retrieve posting list of the token as a 2-dimension array (see comments in MyIndexReader for the structure of the array)
o	int GetDocFreq( String token ): get the document frequency of the token.
o	long GetCollectionFreq( String token ): get the collection frequency of the token.

HW2Main class is the main class for running your tasks. You can find the class in src.zip, and you are NOT allowed to change anything in this file. If you have successfully implemented the classes in task 1&2, you should be able to directly run HW2Main.
Classes.Path contains addresses of all input and output files, so you should put all files in the corresponding directory. Path.java has been updated with two more addresses, so please replace the old one with this new one. You are NOT allowed to change anything in this file, too.

The classes to be implemented can be found in src.zip. You CANNOT change the classes’ names or the required methods’ names. However, you can add new variables, constants, and methods in these classes and create new classes if necessary. 
