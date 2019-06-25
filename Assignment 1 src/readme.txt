Java Version: 10.0.2
Everything is located inside the src folder, it can be executed from HW1Main.java

total document count (text):  503473
totaly document count (web):  198361

avg text corpus running time: 2.4061666666666666 min
avg web corpus running time: 0.47608333333333336 min

total avg time: 3.0 min


Tasks
Task 1: Reading Documents from Collection Files
In this task, you should implement two classes that can read individual documents from trectext and trecweb format collection files (you can find the classes and detailed descriptions in src.zip):
•	PreProcessData.DocumentCollection is a general interface for sequentially reading documents from collection files
•	PreProcessData.TrectextCollection is the class for trectext format
•	PreProcessData.TrecwebCollection is the class for trecweb format
Task 2: Normalize Document Texts
In this task, you should first implement classes to tokenize document texts into individual words, normalize all the words into their lowercase characters, and finally filter stop words.
•	PreProcessData.TextTokenizer is a class for sequentially reading words from a sequence of characters
•	PreProcessData.TextNormalizer is the class that transform each word to its lowercase version, and conduct stemming on each word. 
•	PreProcessData.StopwordsRemover is the class that can recognize whether a word is a stop word or not. A stop word list file will be provided, so that the class should take the stop word list file as input.
