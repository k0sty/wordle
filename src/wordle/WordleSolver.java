package wordle;

import trie.Trie;
import trie.WordWrapper;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordleSolver {

    public static void main(String[] args) throws Exception {
        Trie trie = Trie.fromFile("src/wordle/word_frequency_plurality_list");
        final HashSet<Character> missingChars = Stream.of('e')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('a', 'r', 'o', 's')
                .collect(Collectors.toCollection(HashSet::new));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("-----", wrongSlotChars,
                missingChars);

        System.out.println(potentialWords);

        System.exit(0);
    }
}
