package trie;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import java.io.FileNotFoundException;

class TrieTest {
 
    @Test
    public void singleTrieWordExistenceTest() {
        // Not exhaustive, a single scenario for development consistency
        //  as a first unit test.  That 'ninja' should be part of the SortedSet potentialWords
        final String TARGET_WORD = "ninja";

        trie.Trie trie = null;
        try {
            trie = Trie.fromFile("potential_words");
        } catch (FileNotFoundException e) {
            fail("Word Frequency file was not found");
        }
        final Set<Character> missingChars = Stream.of('r', 'o', 's', 'e', 'f', 'l')
                .collect(Collectors.toCollection(HashSet::new));

        /*
        final Set<Character> wrongSlotChars = Stream.of('a', 'i').collect(Collectors.toCollection(HashSet::new));
        */

        Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
        charGuessesMap.put('a', Stream.of(0)
                .collect(Collectors.toCollection(HashSet::new)));
        charGuessesMap.put('i', Stream.of(3)
                .collect(Collectors.toCollection(HashSet::new)));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("--n--", charGuessesMap,
                missingChars);

        boolean wordExists = false;
        for (WordWrapper currentWrappedWord : potentialWords) {
            System.out.println(currentWrappedWord.getWord());
            if (currentWrappedWord.getWord().equalsIgnoreCase(TARGET_WORD)) {
                wordExists = true;
                break;
            }
        }

        assertEquals(wordExists, true, 
            "Expecting word 'ninja' to exist in Trie for given missingChars, and wrongSlotChars");
    }

}