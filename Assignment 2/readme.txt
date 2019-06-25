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

