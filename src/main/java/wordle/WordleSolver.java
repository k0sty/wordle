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
        final HashSet<Character> missingChars = Stream.of('a', 'r', 's', 'e')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("-----", wrongSlotChars,
                missingChars);

        System.out.println(potentialWords);

        System.exit(0);
    }
}
