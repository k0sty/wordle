package trie;

import java.util.HashMap;
import java.util.Map;

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
