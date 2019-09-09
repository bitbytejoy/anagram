# Anagram Calculator

# Building and running the application

The application is a standard Maven application. It is configured to package a ready JAR as an executable using the Maven Assembly Plugin. To build it start the following command.

    $ mvn package
    
After the build is done, you will need to run the application with the following command:

    $ java -jar target/anagram-jar-with-dependencies.jar -input <path_to_words_file> -output <path_to_results_file>

The input argument is the absolute path to the file containing the list of the words formatted as a word per line. The output argument is the absolute path to the file that is going to contain the resulting lists of anagrams.

In case you are stuck on how to use the application enter the following command:

    $ java -jar target/anagram-jar-with-dependencies.jar -help

## What is it?

This is a basic implementation of an anagram finder in Java. The implementation is made ready to handle large files. It was tested with files as big as 4GB. Although the application can handle large files, it does not offer any performance enhancements. It is still a sequential application processing words one at a time.

## How it works?

The application uses a dependency called MapDB http://www.mapdb.org/. This dependency is an off-heap or on-disk storage. It provides the ability to use large maps like normal Java maps. Without this library, the storage of a huge map of anagrams would be impossible and limited by the computer memory.
    
## Dependencies

- MapDB - on-disk map storage is used to allow for huge file processing inside a console application. Why was this dependency used? There are not a lot of tools like this one. For the use case it was the one that seems like it has the best rating based on their Github repository and resources on the internet.
- Commons CLI - used to handle command line arguments processing. Building a command line arguments parser beats the purpose of building a product. This a popular and easy to use tool that speeds up this kind of task, and that is exactly why it has been used.

# Design

This is a really simple application that needed to be built. No big decisions have been made based on the solution. It is more or less a textbook exercise. It could have been solution could have been even simpler by avoiding the use of the MapDB dependency or even bothering with large files, but I took it as a challenge for myself to build it.

## Maintainability consideration

Maintainability was considered based on this being a command line application:

- The code should be written clearly
- The code should be as simple as possible (KISS principle)
- The code should be prepared for parallel processing in case there is a need to scale into a web service
- Overengineering was avoided at all costs. This is a command line application after all.
- During every project time is the most treasured resource. Spend it wisely.

## Scalability consideration

Scalability has been considered to an extent:

- Scalability of this task means adding more processing power and more data throughput.
- Scaling this kind of task happens horizontally by adding a queuing system and spinning up processors nodes in parallel.
- There will need to exist a queuing/batching system that will prepare words for the processing nodes.
- To scale this application, it needs to be broken up into smaller pieces: a word broker and processing nodes.
- The main code would be usable as part of a processing node because has been built around generic structures (Streams).
- A queuing system would need to be built anew, reading a file and queuing batches of words for processing.

## Performance consideration

Based on the task, no performance consideration was required. There are optimization points:

- If it can be defined that a word can contain specific characters, then the characters could be encoded as prime numbers and the anagram keys would not need to be sorted strings. But, this solution was tested on huge password dictionaries where a word is considered a string of characters that end with a space character.
- The application keeps most of the data in the MapDB on-disk storage to avoid encountering memory overflows for large lists of words.
- Performance could be optimized by splitting input words into smaller chunks of data that are processed in parallel.
- The storage (MapDB) could be replaced by a higher performance database in a production environment where this is not required to be a command line application.

## How would the application be scaled?

### Problems when handling 10 million or even 10 billion words

There are 2 problems that are encountered as the number of input words increases. The processing takes longer and the memory becomes a restriction.

- The processing power problem is solved by batching and processing batches of words in parallel.
- The memory restrictions can only be solved by adding more storage and moving from in-memory data structures to on-disk data structures and databases (depending on the exact requirements).

### How does the application cope with large input files?

In case that there is enough storage on the disk, the application will handle the large files correctly. This is bound by the system and the addressable memory again. The biggest test file used was a 14GB password dictionary containing over 1.5 billion lines. The longest that I let it run was for 15 mins. It did not finish processing but it ran successfully. The largest successful test has been done using a 10 million lines password dictionary. On the test machine it took about 5min 30s to complete.

### How would you scale the application to a 10 billion words processor?

Taking this application to a processor that can handle 10 billion words with high performance requires a high performance queuing system with a huge number of processing nodes. The exact number cannot be given because it again depends on benchmarking the exact hardware that is at your disposal.