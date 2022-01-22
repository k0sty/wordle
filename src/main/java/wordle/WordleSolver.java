package wordle;

import trie.Trie;
import trie.WordWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordleSolver {

    public static void main(String[] args) throws Exception {
        // best initial guess: arose

        String word = "chill";

        Trie trie = Trie.fromFile("word_frequency_plurality_list");
        final HashSet<Character> missingChars = Stream.of('a', 'r', 's', 'm', 'o', 'v', 't', 'l', 'h')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
        charGuessesMap.put('n', Stream.of(0)
                .collect(Collectors.toCollection(HashSet::new)));
        charGuessesMap.put('c', Stream.of(2)
                .collect(Collectors.toCollection(HashSet::new)));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("-i--e", charGuessesMap,
                missingChars);

        System.out.println(potentialWords);

        System.exit(0);
    }
}
