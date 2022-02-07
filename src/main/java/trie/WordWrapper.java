package trie;

import java.util.Objects;

public class WordWrapper implements Comparable<WordWrapper>{
    private final String word;
    private final double frequency;
    private final boolean plural;

    private WordWrapper(String word, double frequency, boolean plural) {
        this.word = word;
        this.frequency = frequency;
        this.plural = plural;
    }

    public static WordWrapper fromLine(String line) {
        String[] arr = line.split(" ");

        return new WordWrapper(line.trim(), 0d, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordWrapper)) return false;
        WordWrapper that = (WordWrapper) o;
        return Objects.equals(word, that.word);

    }

    public String getWord() {
        return word;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, frequency);
    }

    @Override
    public int compareTo(WordWrapper other) {

        // prioritize singular words
        if (this.plural && !other.plural) {
            return -1;
        }

        if (this.frequency == other.frequency) {
            return this.word.compareTo(other.word);
        }

        return Double.compare(frequency, other.frequency);
    }

    @Override
    public String toString() {
        return word;
    }
}