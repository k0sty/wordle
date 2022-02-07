package wordle;

import trie.Trie;
import trie.WordWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordleSolver {

    public static void main(String[] args) throws Exception {
        // best initial guess: arose
        String word = "chill";

        long t1 = System.currentTimeMillis();
        Trie trie = Trie.fromFile("potential_words");
        final HashSet<Character> missingChars = Stream.of('s', 'e')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
        charGuessesMap.put('a', Stream.of(2)
                .collect(Collectors.toCollection(HashSet::new)));
        charGuessesMap.put('t', Stream.of(3)
                .collect(Collectors.toCollection(HashSet::new)));

        final SortedSet<WordWrapper> potentialWords =
                trie.generatePotentialWords("-l---", charGuessesMap, missingChars);

        System.out.println(potentialWords);
        System.out.println(potentialWords.size());
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        System.exit(0);
    }
}
