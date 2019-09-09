package bitbytejoy;

import com.google.common.collect.Lists;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class AnagramCalculator {
    /**
     * Used to find anagrams in a stream of words.
     *
     * @param wordsStream - Any stream of words that requires anagrams to be found.
     * @param anagramsStore - Output of the anagrams, key is the sorted anagram letters and value is a serialized list of words that are anagrams.
     */
    public void findAnagrams(Stream<String> wordsStream, Map<String, String> anagramsStore) {
        wordsStream.forEach(word -> {
            String anagramKey = calcAnagramKey(word);

            if (anagramKey.isEmpty()) return;

            String anagrams = anagramsStore.getOrDefault(anagramKey, null);
            if (anagrams == null) {
                anagramsStore.put(anagramKey, word);
            } else {
                List<String> anagramsList = Lists.newArrayList(anagrams.split(" "));
                anagramsList.add(word);
                anagramsStore.put(anagramKey, String.join(" ", anagramsList));
            }
        });
    }

    /**
     * Used to find anagrams in a list of words.
     *
     * @param words
     * @param anagramsStore
     */
    public void findAnagrams(List<String> words, Map<String, String> anagramsStore) {
        findAnagrams(words.stream(), anagramsStore);
    }

    /**
     * Finds anagrams in a file. Every line of the file should represent a word.
     *
     * @param wordsFilePath
     * @param anagramsStore
     * @throws IOException
     */
    public void findAnagrams(String wordsFilePath, Map<String, String> anagramsStore) throws IOException {
        findAnagrams(Files.lines(Paths.get(wordsFilePath)), anagramsStore);
    }

    /**
     * Prints the anagrams from a map onto the STD OUT. Every line is a list of anagrams.
     *
     * @param anagramsStore
     */
    public void printAnagrams(Map<String, String> anagramsStore) {
        anagramsStore.forEach((key, anagrams) -> {
            if (anagrams.contains(" ")) System.out.println(anagrams);
        });
    }

    /**
     * Writes all anagrams from a map into to a file. Every line is a list of anagrams.
     *
     * @param anagramsStore
     * @param filePath
     * @throws IOException
     */
    public void printAnagramsToFile(Map<String, String> anagramsStore, String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
        Files.createFile(Paths.get(filePath));
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(filePath));
        anagramsStore.forEach((key, anagrams) -> {
            if (anagrams.contains(" ")) {
                try {
                    bufferedWriter.append(anagrams).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        bufferedWriter.close();
    }

    /**
     * Calculates a unique anagram key for the word.
     *
     * @param word
     * @return
     */
    public String calcAnagramKey(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }
}