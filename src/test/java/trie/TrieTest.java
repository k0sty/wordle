package trie;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.SortedSet;
import java.util.HashSet;
import java.util.Iterator;
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
            trie = Trie.fromFile("word_frequency_plurality_list");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        final HashSet<Character> missingChars = Stream.of('r', 'o', 's', 'e', 'f', 'l')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('a', 'i')
                .collect(Collectors.toCollection(HashSet::new));
        final SortedSet<WordWrapper> potentialWords = trie.generatePotentialWords("--n--", wrongSlotChars,
                missingChars);

        boolean wordExists = false;
        Iterator<WordWrapper> iterator = potentialWords.iterator();
        while (iterator.hasNext()) {
            WordWrapper currentWrappedWord = iterator.next();
            System.out.println(currentWrappedWord.getWord());
            if ( currentWrappedWord.getWord().equalsIgnoreCase(TARGET_WORD) ) {
                wordExists = true;
            }
        }

        assertEquals(wordExists, true, 
            "Expecting word 'ninja' to exist in Trie for given missingChars, and wrongSlotChars");
    }

}