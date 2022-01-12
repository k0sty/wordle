package trie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trie {
    private TrieLevel rootTrieLevel;

    public Trie() {
        rootTrieLevel = new TrieLevel();
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
                TrieLevel newLevel = isWord? new TrieLevel(isWord) : new TrieLevel();
                currentMap.put(curr, newLevel);
                currentLevel = newLevel;
            }
        }
    }

//    public Set<String> generatePotentialWordsHelper(String currentGuess, StringBuilder currentWord, )

//    public Set<String> generatePotentialWords(String currentGuess, Set<Character> wrongLocation,
//                                              Set<Character> notPresent) {
//        Set <String> ret = new HashSet<>();
//        TrieLevel currentLevel = rootTrieLevel;
//        for (int i = 0; i < currentGuess.length(); i ++) {
//            char curr = currentGuess.charAt(i);
//            if ()
//        }
//        return ret;
//    }

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
