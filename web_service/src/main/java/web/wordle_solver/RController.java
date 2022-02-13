package web.wordle_solver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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
public class RController {

	@GetMapping("/responseTest")
	public String index() {
		return "Index of WorldSolver!";
	}

	// for demonstration purposes only
	@GetMapping("/demoTrie")
	public ResponseEntity<String> demoTrie(@RequestParam String missingCharsCSV,
			@RequestParam String charGuessesMapCSV,
			@RequestParam String currentGuess) {

		// missingChars
		HashSet<Character> missingChars = new HashSet(26);
		StringTokenizer st = new StringTokenizer(missingCharsCSV, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken();

			// Needs to be a single character
			if (nextToken.length()>1) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("missingCharsCSV contains non-char: " + nextToken);
			}

			missingChars.add(nextToken.charAt(0));
		}

		// charGuessesMap, parsed of the form "charGuessesMapCSV=0n,2c"
		Map<Character, Set<Integer>> charGuessesMap = new HashMap<>();
		st = new StringTokenizer(charGuessesMapCSV, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken();

			if (nextToken.length()>2) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("charGuessesMapCSV contains bad map pair: " + nextToken);
			}
			
			int mapIndex = Character.getNumericValue( nextToken.charAt(0) );
			char guessedChar = nextToken.charAt(1);
			charGuessesMap.put(guessedChar, Stream.of(mapIndex).collect(Collectors.toCollection(HashSet::new)));

		}

		// currentGuess
		if (currentGuess.length()!=5) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currentGuess is invalid: " + currentGuess);
		}

		SingletonSteward singletonSteward = SingletonSteward.getInstance();
        final SortedSet<WordWrapper> potentialWords = singletonSteward.trie.generatePotentialWords(currentGuess, charGuessesMap, missingChars);
		String potentialWordsJSON = Serializer.createJSONString(potentialWords);

		return ResponseEntity.ok(potentialWordsJSON);
	}

}