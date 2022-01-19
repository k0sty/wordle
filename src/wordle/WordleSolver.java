package wordle;

import trie.Trie;
import trie.WordWrapper;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordleSolver {

    public static void main(String[] args) throws Exception {
        // best initial guess: arose

        String word = "chill";

        Trie trie = Trie.fromFile("word_frequency_plurality_list");
        final HashSet<Character> missingChars = Stream.of('l')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('a', 'r', 'o', 's')
                .collect(Collectors.toCollection(HashSet::new));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("-----", wrongSlotChars,
                missingChars);

        System.out.println(potentialWords);

        System.exit(0);
    }
}
