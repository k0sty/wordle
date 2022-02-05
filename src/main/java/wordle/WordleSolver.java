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

        Stack<String> asdf = new Stack<>();
        asdf.add("a");
        asdf.add("b");
        asdf.add("c");

        //System.out.println(asdf.pop());
        //System.exit(0);


        String word = "chill";

        long t1 = System.currentTimeMillis();
        Trie trie = Trie.fromFile("word_frequency_plurality_list");
        final HashSet<Character> missingChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("-----", charGuessesMap, missingChars);

        System.out.println(potentialWords);
        System.out.println(potentialWords.size());
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        System.exit(0);
    }
}
