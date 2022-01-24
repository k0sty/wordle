package wordle;

import com.google.common.base.MoreObjects;
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


    static class TenSmallestSet {
        final TreeSet<Integer> set;

        public TenSmallestSet() {
            this.set = new TreeSet<>();
        }

        public void add(int intToAdd) {
            if (set.size() < 10) {
                set.add(intToAdd);
            } else if (intToAdd < set.last()) {
                set.pollLast();
                set.add(intToAdd);
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

        String best;
        for (String word : getSetOfPastWinners()) {

            for (String currentWord : getAllFiveLetterWords()) {
                WordlePlayer player = new WordlePlayer(word, trie);
                if (!currentWord.equals(word)) {
                    player.checkWord(currentWord, false);
                    if (map.containsKey(currentWord)) {
                        map.get(currentWord).add(player.getPossibilities().size());
                    } else {
                        final TenSmallestSet set = new TenSmallestSet();
                        set.add(player.getPossibilities().size());
                        map.put(currentWord, set);

                    }
                }
            }

            //System.out.println(map);
            //System.exit(0);
        }
        System.out.println(map);
    }

}
