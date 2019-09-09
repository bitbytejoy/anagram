package bitbytejoy;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class AnagramCalculatorTest
{
    private static AnagramCalculator anagramCalculator;

    @BeforeClass
    public static void beforeAll() {
        anagramCalculator = new AnagramCalculator();
    }

    @Test
    public void findAnagramsShouldFindAllAnagramsInMemory() {
        List<String> words = new ArrayList<>();
        words.add("act");
        words.add("cat");
        words.add("tree");
        words.add("race");
        words.add("care");
        words.add("acre");
        words.add("bee");

        Map<String, String> anagrams = new HashMap<>();
        anagramCalculator.findAnagrams(words, anagrams);

        Assert.assertEquals(4, anagrams.size());
        Assert.assertEquals(anagrams.get("act"), "act cat");
        Assert.assertEquals(anagrams.get("eert"), "tree");
        Assert.assertEquals(anagrams.get("acer"), "race care acre");
        Assert.assertEquals(anagrams.get("bee"), "bee");
    }

    @Test
    public void findAnagramsShouldFindAllAnagramsInAFile() throws IOException {
        String samplePath = getClass().getClassLoader().getResource("sample.txt").getPath();
        Files.deleteIfExists(Paths.get("test.db"));
        DB db = DBMaker.fileDB("test.db").make();
        ConcurrentMap anagrams = db.hashMap("map").createOrOpen();

        anagramCalculator.findAnagrams(samplePath, anagrams);

        Assert.assertEquals(4, anagrams.size());
        Assert.assertEquals(anagrams.get("act"), "act cat");
        Assert.assertEquals(anagrams.get("eert"), "tree");
        Assert.assertEquals(anagrams.get("acer"), "race care acre");
        Assert.assertEquals(anagrams.get("bee"), "bee");

        db.close();
        Files.deleteIfExists(Paths.get("test.db"));
    }
}
