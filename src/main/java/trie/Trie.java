package trie;

import utils.StreamResources;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Trie {
    private final trie.TrieLevel rootTrieLevel;
    private final Map<String, WordWrapper> statsMap;


    private Trie() {
        statsMap = new HashMap<>();
        rootTrieLevel = new TrieLevel(0);
    }

    public static Trie fromFile(String resourceName) throws FileNotFoundException {
        Trie ret = new Trie();

        InputStream inputStream = StreamResources.getFileFromResourceAsStream(resourceName);

        Scanner freqScanner = new Scanner(inputStream);
        while(freqScanner.hasNextLine()) {
            WordWrapper wrapper = WordWrapper.fromLine(freqScanner.nextLine().trim());
            ret.statsMap.put(wrapper.getWord(), wrapper);
            ret.addWord(wrapper.getWord());
        }

        return ret;
    }

    private void addWord(String word) {
        TrieLevel currentLevel = rootTrieLevel;
        for (int i = 0; i < word.length(); i++) {
            char curr = word.charAt(i);
            boolean isWord = (i == word.length()-1);
            final Map<Character, TrieLevel> currentMap = currentLevel.getTrieLevelMap();
            if (currentMap.containsKey(curr)) {
                final TrieLevel trieLevel = currentMap.get(curr);
                trieLevel.setIsWord(isWord);
                currentLevel = trieLevel;
            } else {
                TrieLevel newLevel = isWord? new TrieLevel(i+1, isWord) : new TrieLevel(i+1);
                currentMap.put(curr, newLevel);
                currentLevel = newLevel;
            }
        }
    }

    private boolean doesWordContainAllChars(String word, Map<Character, Set<Integer>> charGuessesMap) {

        for (Character character : charGuessesMap.keySet()) {
            if (!word.contains("" + character)) {
                return false;
            }
        }

        return true;
    }

    private Set<String> generatePotentialWordsHelper(Set<String> guesses,
                                                     String currentGuess,
                                                     StringBuilder currentWord,
                                                     TrieLevel currentLevel,
                                                     Map<Character, Set<Integer>> charGuessesMap,
                                                     Set<Character> notPresent) {
        Set<String> ret = new HashSet<>();


        if (currentLevel.getLevelNumber() > 0 ) {
            final char currentKnownChar = currentGuess.charAt(currentLevel.getLevelNumber()-1);
            final char latestCharacter = currentWord.charAt(currentLevel.getLevelNumber()-1);
            if (currentWord.length() > 0
                    && currentKnownChar != '-' &&
                    latestCharacter != currentKnownChar) {
                return ret;
            }
        }

        for (int i = currentLevel.getLevelNumber(); i < currentGuess.length(); i++) {
            char curr = currentGuess.charAt(i);
            final Map<Character, TrieLevel> currentTrieLevelMap = currentLevel.getTrieLevelMap();
            if (curr == '-') {
                for (Character potentialChar : currentTrieLevelMap.keySet()) {
                    if (charGuessesMap.containsKey(potentialChar) && charGuessesMap.get(potentialChar).contains(currentWord.length())) {
                        continue;
                    } else if (!notPresent.contains(potentialChar)) {
                        final StringBuilder potentialWord =
                                new StringBuilder().append(currentWord).append(potentialChar);
                        if (currentTrieLevelMap.get(potentialChar).isWord()) {
                            if (doesWordContainAllChars(potentialWord.toString(), charGuessesMap)) {
                                if (ret.contains(potentialWord.toString())) {
                                    System.out.println("ADDING THE SAME WORD TWICE");
                                }
                                ret.add(potentialWord.toString());
                            }
                        }
                        if (!guesses.contains(potentialWord.toString())) {
                            guesses.add(potentialWord.toString());
                            ret.addAll(generatePotentialWordsHelper(guesses, currentGuess,
                                    potentialWord,
                                    currentTrieLevelMap.get(potentialChar),
                                    charGuessesMap, notPresent));
                        }

                    }
                }
            } else {
                if (currentTrieLevelMap.containsKey(curr)) {
                    final StringBuilder potentialWord =
                            new StringBuilder().append(currentWord).append(curr);
                    if (currentTrieLevelMap.get(curr).isWord()) {
                        if (doesWordContainAllChars(potentialWord.toString(), charGuessesMap)) {
                            if (ret.contains(potentialWord.toString())) {
                                System.out.println("ADDING THE SAME WORD TWICE");
                            }
                            ret.add(potentialWord.toString());
                        }
                    }
                    if (!guesses.contains(potentialWord.toString())) {
                        guesses.add(potentialWord.toString());
                        ret.addAll(generatePotentialWordsHelper(guesses, currentGuess,
                                potentialWord,
                                currentTrieLevelMap.get(curr), charGuessesMap, notPresent));
                    }

                }
            }
        }

        return ret;
    }

    public PotentialWordsWrapper generatePotentialWords(String currentGuess, Map<Character, Set<Integer>> charGuessesMap,
                                              Set<Character> notPresent) {

        PotentialWordsWrapper wrapper = new PotentialWordsWrapper(currentGuess);
        for (String str : generatePotentialWordsHelper(new HashSet<>(), currentGuess, new StringBuilder(), rootTrieLevel,
                charGuessesMap, notPresent)) {
            wrapper.addWord(statsMap.get(str));
        }

        return wrapper;
    }

    public boolean checkIfPresent(String word) {

        TrieLevel currentLevel = rootTrieLevel;
        for (int i = 0; i < word.length(); i++) {
            char currChar = word.charAt(i);
            final Map<Character, TrieLevel> currentMap = currentLevel.getTrieLevelMap();
            if (!currentMap.containsKey(currChar)) {
                return false;
            } else if (i == word.length()-1){
                // we are at the last character. this is a special case
                TrieLevel nextlevel = currentMap.get(currChar);
                return nextlevel.isWord();
            }
            currentLevel = currentMap.get(currChar);
        }
        return false;

    }

    public void printAllWords() {
        TrieLevel currentLevel = rootTrieLevel;
        for (Character curr: currentLevel.getTrieLevelMap().keySet()) {
            printAllWordsHelper(new StringBuilder().append(curr), currentLevel.getTrieLevelMap().get(curr));
        }
    }

    private void printAllWordsHelper(StringBuilder builder, TrieLevel currentLevel) {

        if (currentLevel.isWord()) {
            System.out.println(builder.toString());

        }

        for (Character curr : currentLevel.getTrieLevelMap().keySet()) {
            printAllWordsHelper(new StringBuilder().append(builder).append(curr), currentLevel.getTrieLevelMap().get(curr));
        }
    }



}
