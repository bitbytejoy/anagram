package bitbytejoy;

import org.apache.commons.cli.*;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class App {
    public static void main( String[] args ) throws IOException {
        // arguments parsing
        Arguments arguments = new Arguments();
        CommandLine cmd = arguments.parseArguments(args);

        // initialization
        final String storeName = "anagrams.db";
        String wordsFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        AnagramCalculator anagramCalculator = new AnagramCalculator();

        Files.deleteIfExists(Paths.get(storeName));
        DB db = DBMaker.fileDB(storeName).make();
        Map anagramStore = db.hashMap("map").createOrOpen();

        // execution
        anagramCalculator.findAnagrams(wordsFilePath, anagramStore);
        anagramCalculator.printAnagrams(anagramStore);
        if (outputFilePath != null) {
            try {
                anagramCalculator.printAnagramsToFile(anagramStore, outputFilePath);
            } catch (IOException e) {
                System.err.println("There was an error trying to save your the result to the output file specified.");
            }
        }

        // clean up
        db.close();
        Files.deleteIfExists(Paths.get(storeName));
    }
}
