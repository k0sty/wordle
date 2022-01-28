package web.utils;

import web.wordle_solver.SingletonSteward;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import trie.WordWrapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ./gradlew clean test --tests SerializerTests -i

public class SerializerTests {

        @Test
        public void testParams()  {

                SingletonSteward singletonSteward = SingletonSteward.getInstance();

                final HashSet<Character> missingChars = Stream.of('a', 'r', 's', 'm', 'o', 'v', 't', 'l', 'h')
                        .collect(Collectors.toCollection(HashSet::new));
                final HashSet<Character> wrongSlotChars = Stream.of('o')
                        .collect(Collectors.toCollection(HashSet::new));
                Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
                charGuessesMap.put('n', Stream.of(0)
                        .collect(Collectors.toCollection(HashSet::new)));
                charGuessesMap.put('c', Stream.of(2)
                        .collect(Collectors.toCollection(HashSet::new)));
                final SortedSet<WordWrapper> potentialWords = singletonSteward.trie.generatePotentialWords("-i--e", charGuessesMap, missingChars);

                String jsonOfPotentialWords = Serializer.createJSONString(potentialWords);

                assertEquals(jsonOfPotentialWords,"{\"potentialWords\":[\"wince\"]}", "generated JSON does not match up");
        }

}
