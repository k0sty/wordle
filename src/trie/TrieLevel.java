package trie;

import java.util.HashMap;
import java.util.Map;

class TrieLevel {
    private final Map<Character, TrieLevel> trieLevelMap;
    private boolean isWord;
    private int levelNumber;

    public TrieLevel (int levelNumber) {
        this.trieLevelMap = new HashMap<>();
        this.levelNumber = levelNumber;
    }

    TrieLevel (int levelNumber, boolean isWord) {
        this(levelNumber);
        this.isWord = isWord;
    }

    Map<Character, TrieLevel> getTrieLevelMap() {
        return trieLevelMap;
    }

    void setIsWord(boolean isWord) {
        if (!this.isWord) {
            this.isWord = isWord;
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    boolean isWord() {
        return isWord;
    }
}