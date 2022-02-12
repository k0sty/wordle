package wordle;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import trie.Trie;
import trie.WordWrapper;
import utils.StreamResources;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class FindBestStartingWord {

    private static Set<String> getAllFiveLetterWords() {
        Set<String> ret = new HashSet<>();
        InputStream inputStream = StreamResources.getFileFromResourceAsStream("potential_words");

        Scanner freqScanner = new Scanner(inputStream);
        while(freqScanner.hasNextLine()) {
            ret.add(freqScanner.nextLine().trim());
        }
        return ret;

    }

    private static Set<String> getSetOfPastWinners() {
        Set<String> ret = new HashSet<>();
        InputStream inputStream = StreamResources.getFileFromResourceAsStream("past_winners");

        Scanner freqScanner = new Scanner(inputStream);
        while(freqScanner.hasNextLine()) {
            ret.add(freqScanner.nextLine().trim());
        }
        return ret;
    }

    static class WordStruct {
        final String word;
        final List<Integer> numWordsRemainingAfterGuess;
        final List<Integer> numCharsInRightSpotAfterGuess;

        public WordStruct(String word, int numWordsRemainingAfterGuess, int numCharsInRightSpotAfterGuess) {
            this.word = word;

            this.numWordsRemainingAfterGuess = new LinkedList<>();
            this.numCharsInRightSpotAfterGuess = new LinkedList<>();

            this.numWordsRemainingAfterGuess.add(numWordsRemainingAfterGuess);
            this.numCharsInRightSpotAfterGuess.add(numCharsInRightSpotAfterGuess);
        }

        public void addEntry(int numWordsRemainingAfterGuess, int numCharsInRightSpotAfterGuess) {
            this.numWordsRemainingAfterGuess.add(numWordsRemainingAfterGuess);
            this.numCharsInRightSpotAfterGuess.add(numCharsInRightSpotAfterGuess);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WordStruct that = (WordStruct) o;
            return Objects.equal(word, that.word);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(word);
        }

        public List<Integer> getNumWordsRemainingAfterGuess() {
            return numWordsRemainingAfterGuess;
        }

        public List<Integer> getNumCharsInRightSpotAfterGuess() {
            return numCharsInRightSpotAfterGuess;
        }
    }

    public static void main(String[] args) throws Exception {

        Trie trie = Trie.fromFile("potential_words");

        Map<String, WordStruct> map = new HashMap<>();

        for (String word : getAllFiveLetterWords()) {
        //for (String word : getSetOfPastWinners()) {

            //for (String potentialStartingWord : getSetOfPastWinners()) {
            for (String potentialStartingWord : getAllFiveLetterWords()) {
                WordlePlayer player = new WordlePlayer(word, trie);
                if (!potentialStartingWord.equals(word)) {
                    player.checkWord(potentialStartingWord, false);
                    if (map.containsKey(potentialStartingWord)) {

                        map.get(potentialStartingWord).addEntry(player.getPossibilities().getWordsSet().size(), player.getCharsInRightSpot());
                    } else {
                        WordStruct struct = new WordStruct(potentialStartingWord, player.getPossibilities().getWordsSet().size(), player.getCharsInRightSpot());
                        map.put(potentialStartingWord, struct);

                    }
                }
            }
        }

        for (String key : map.keySet()) {

            final OptionalDouble averageRemainingWords = map.get(key).getNumWordsRemainingAfterGuess().stream()
                    .mapToDouble(a -> a)
                    .average();
            final OptionalDouble averageCharsInRightPlace = map.get(key).getNumCharsInRightSpotAfterGuess().stream()
                    .mapToDouble(a -> a)
                    .average();
            System.out.printf("%s %s %s %n", key, averageRemainingWords.toString(), averageCharsInRightPlace.toString());
        }

    }

}
