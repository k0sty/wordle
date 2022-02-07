package web.wordle_solver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.utils.Serializer;

import java.util.StringTokenizer;

// for demonstration purposes only
import trie.WordWrapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class Controller {

	@GetMapping("/")
	public String index() {
		return "Index of WorldSolver!";
	}

	// for demonstration purposes only
	@GetMapping("/demoTrie")
	public String demoTrie(@RequestParam String missingCharsCSV) {

		System.out.println(missingCharsCSV);
		HashSet<Character> missingChars = new HashSet(26);
		StringTokenizer st = new StringTokenizer(missingCharsCSV, ",");
		while (st.hasMoreTokens()) {
			//TODO, check if length too long....., raise exception
			missingChars.add(st.nextToken().charAt(0));
		}

		SingletonSteward singletonSteward = SingletonSteward.getInstance();

        final HashSet<Character> wrongSlotChars = Stream.of('o')
                .collect(Collectors.toCollection(HashSet::new));
        Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
        charGuessesMap.put('n', Stream.of(0)
                .collect(Collectors.toCollection(HashSet::new)));
        charGuessesMap.put('c', Stream.of(2)
                .collect(Collectors.toCollection(HashSet::new)));
        final SortedSet<WordWrapper> potentialWords = singletonSteward.trie.generatePotentialWords("-i--e", charGuessesMap,
                missingChars);

		String potentialWordsJSON = Serializer.createJSONString(potentialWords);

		return potentialWordsJSON;
	}

}