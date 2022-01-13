package trie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trie {
    private TrieLevel rootTrieLevel;

    public Trie() {
        rootTrieLevel = new TrieLevel(0);
    }

    public void addWord(String word) {
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

    private boolean doesWordContainAllChars(String word, Set<Character> characterSet) {

        for (Character character : characterSet) {
            if (!word.contains("" + character)) {
                return false;
            }
        }

        return true;
    }

    private Set<String> generatePotentialWordsHelper(String currentGuess,
                                                     StringBuilder currentWord,
                                                     TrieLevel currentLevel,
                                                     Set<Character> wrongLocation,
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
                    if (!notPresent.contains(potentialChar)) {
                        final StringBuilder potentialWord =
                                new StringBuilder().append(currentWord).append(potentialChar);
                        if (currentTrieLevelMap.get(potentialChar).isWord()) {
                            if (doesWordContainAllChars(potentialWord.toString(), wrongLocation)) {
                                ret.add(potentialWord.toString());
                            }
                        }
                        ret.addAll(generatePotentialWordsHelper(currentGuess,
                                potentialWord,
                                currentTrieLevelMap.get(potentialChar),
                                wrongLocation, notPresent));
                    }
                }
            } else {
                if (currentTrieLevelMap.containsKey(curr)) {
                    final StringBuilder potentialWord =
                            new StringBuilder().append(currentWord).append(curr);
                    if (currentTrieLevelMap.get(curr).isWord()) {
                        if (doesWordContainAllChars(potentialWord.toString(), wrongLocation)) {
                            ret.add(potentialWord.toString());
                        }
                    }
                    ret.addAll(generatePotentialWordsHelper(currentGuess,
                            potentialWord,
                            currentTrieLevelMap.get(curr), wrongLocation, notPresent));
                }
            }
        }

        return ret;
    }

    public Set<String> generatePotentialWords(String currentGuess, Set<Character> wrongLocation,
                                              Set<Character> notPresent) {

        return generatePotentialWordsHelper(currentGuess, new StringBuilder(), rootTrieLevel,
                wrongLocation, notPresent);
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
