package wordle;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.sun.source.tree.Tree;
import trie.Trie;
import trie.WordWrapper;
import utils.StreamResources;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class FindBestStartingWord {

    private static Set<String> getAllFiveLetterWords() {
        Set<String> ret = new HashSet<>();
        InputStream inputStream = StreamResources.getFileFromResourceAsStream("words_list");

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

    static class WordCountWrapper implements Comparable<WordCountWrapper>{
        final String word;
        final int count;

        public WordCountWrapper(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public int getCount() {
            return count;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WordCountWrapper that = (WordCountWrapper) o;

            return Objects.equal(this.word, that.word) &&
                    Objects.equal(this.count, that.count);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(word, count);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("word", word)
                    .add("count", count)
                    .toString();
        }

        @Override
        public int compareTo(WordCountWrapper o) {
            return Integer.compare(getCount(), o.getCount());
        }
    }

    static class TenSmallestSet {
        final TreeSet<WordCountWrapper> set;

        public TenSmallestSet() {
            this.set = new TreeSet<>();
        }

        public void add(WordCountWrapper wordToAdd) {
            if (set.size() < 10) {
                set.add(wordToAdd);
            } else if (wordToAdd.getCount() < set.last().getCount()) {
                set.pollLast();
                set.add(wordToAdd);
            }
        }

        @Override
        public String toString() {
            return set.toString();
        }


    }

    public static void main(String[] args) throws Exception {

        Trie trie = Trie.fromFile("word_frequency_plurality_list");

        Map<String, TenSmallestSet> map = new HashMap<>();

        int i = 0;
        String best;
        for (String word : getSetOfPastWinners()) {

            for (String currentWord : getAllFiveLetterWords()) {
                WordlePlayer player = new WordlePlayer(word, trie);
                if (!currentWord.equals(word)) {
                    player.checkWord(currentWord, false);
                    if (map.containsKey(word)) {
                        map.get(word).add(new WordCountWrapper(currentWord, player.getPossibilities().size()));
                    } else {
                        final TenSmallestSet set = new TenSmallestSet();
                        set.add(new WordCountWrapper(currentWord, player.getPossibilities().size()));
                        map.put(word, set);

                    }
                }
                i++;
                if (i > 3) {

                    System.exit(0);
                }
                //System.out.println(map);
                //System.exit(0);
            }

            //System.out.println(map);
            //System.exit(0);
        }
        System.out.println(map);
    }

}
