package trie;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class PotentialWordsWrapper {
    final String currentGuess;
    final TreeSet<WordWrapper> set;
    final Map<Character, Integer> map;
    double totalChars = 0;

    public PotentialWordsWrapper(String currentGuess) {
        this.currentGuess = currentGuess;
        this.set = new TreeSet<>();
        this.map = new HashMap<>();
    }

    public void addWord(WordWrapper word) {
        set.add(word);
        for (int i = 0; i < currentGuess.length(); i++) {
            if (currentGuess.charAt(i) == '-') {
                char guessChar = word.getWord().charAt(i);
                if (map.containsKey(guessChar)) {
                    map.put(guessChar, map.get(guessChar) + 1);
                } else {
                    map.put(guessChar, 1);
                }
                totalChars++;
            }
        }
    }

    public void printLetterRatios() {
        Map<Character, Double> ratiosMap = new HashMap<>();

        for (Character currChar : map.keySet()) {
            ratiosMap.put(currChar, map.get(currChar) / totalChars);
        }

        System.out.println(ratiosMap);
    }

    public TreeSet<WordWrapper> getWordsSet() {
        return set;
    }
}