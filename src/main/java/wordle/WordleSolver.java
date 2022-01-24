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
        final HashSet<Character> missingChars = Stream.of('l', 'e', 'd', 't', 'a', 'n', 'b', 'k')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
        //charGuessesMap.put('r', Stream.of(0).collect(Collectors.toCollection(HashSet::new)));
        charGuessesMap.put('c', Stream.of(3)
                .collect(Collectors.toCollection(HashSet::new)));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("-ri--", charGuessesMap,
                missingChars);

        System.out.println(potentialWords);

        System.exit(0);
    }
}
