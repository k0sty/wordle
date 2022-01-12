package trie;

import java.util.HashMap;
import java.util.Map;

class TrieLevel {
    private final Map<Character, TrieLevel> trieLevelMap;
    private boolean isWord;
    private int value = 0;

    public TrieLevel () {
        this.trieLevelMap = new HashMap<>();
    }

    TrieLevel (boolean isWord, int value) {
        this();
        this.isWord = isWord;
        this.value = value;
    }

    Map<Character, TrieLevel> getTrieLevelMap() {
        return trieLevelMap;
    }

    void setIsWord(boolean isWord, int value) {
        if (!this.isWord) {
            this.isWord = isWord;
        }
        if (isWord) {
            this.value += value;
        }
    }

    boolean isWord() {
        return isWord;
    }

    int getValue() {
        return value;
    }
}