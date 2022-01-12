package trie;

import java.util.HashMap;
import java.util.Map;

class TrieLevel {
    private final Map<Character, TrieLevel> trieLevelMap;
    private boolean isWord;

    public TrieLevel () {
        this.trieLevelMap = new HashMap<>();
    }

    TrieLevel (boolean isWord) {
        this();
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

    boolean isWord() {
        return isWord;
    }
}