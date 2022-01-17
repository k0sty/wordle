package wordle;


import trie.Trie;
import trie.WordWrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordlePlayer {
    private final static int MAX_GESSES = 6;
    private final static int WORD_LENGTH = 5;

    private final StringBuilder currentState;
    private String currentGuess = "-----";
    private final String word;
    private final Trie trie;
    private final Map<Character, Integer> charCountMap;
    private final Map<Character, Set<Integer>> charGuessesMap;
    private final SortedSet<Character> charsInWrongSpot;
    private final Set<Character> charsNotToAddToWrongSpotSet;
    private final SortedSet<Character> charsNotInWordSet;


    public WordlePlayer(String word, String filePath) throws FileNotFoundException {
        this.trie = Trie.fromFile(filePath);
        validateWord(word, trie);

        this.word = word;
        this.charsNotToAddToWrongSpotSet = new HashSet<>();
        this.currentState = new StringBuilder("-----");
        charGuessesMap = new HashMap<>();
        charCountMap = getMapFromWord(word);
        charsNotInWordSet = new TreeSet<>();
        charsInWrongSpot = new TreeSet<>();
    }

    private static Map<Character, Integer> getMapFromWord(String word) {
        Map<Character, Integer> ret = new HashMap<>();
        for (Character curr : word.toCharArray()) {
            if (ret.containsKey(curr)) {
                ret.put(curr, ret.get(curr)+1);
            } else {
                ret.put(curr, 1);
            }

        }
        return ret;
    }

    private static void validateWord(String word, Trie trie) {
        if (word.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("The guesses should be " + WORD_LENGTH +
                    " characters long");
        }

        if (!trie.checkIfPresent(word)) {
            throw new IllegalArgumentException(word + " is not a valid " + WORD_LENGTH +
                    " letter word");
        }
    }

    public void printPossibilities() {
        System.out.println(trie.generatePotentialWords(currentState.toString(), charsInWrongSpot, charsNotInWordSet));
    }

    public void checkWord(String guess) {

        validateWord(guess, trie);

        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == word.charAt(i)) {
                currentState.setCharAt(i, guessChar);
            } else if (charCountMap.containsKey(guessChar) &&
                    !charsNotToAddToWrongSpotSet.contains(guessChar)){
                charsInWrongSpot.add(guessChar);
            } else {
                charsNotInWordSet.add(guessChar);
            }
        }


        final Map<Character, Integer> currentGuessMap = getMapFromWord(currentState.toString());
        for (Character curr : currentGuessMap.keySet()) {
            if (curr != '-') {
                if (charCountMap.containsKey(curr) && charCountMap.get(curr).equals(currentGuessMap.get(curr))) {
                    charsNotToAddToWrongSpotSet.add(curr);
                    charsInWrongSpot.remove(curr);
                }
            }
        }

        System.out.printf("%s ... Wrong Spot: %s Not Present: %s%n",
                currentState, charsInWrongSpot.toString(),
                charsNotInWordSet.toString());
    }



    private static String getRandomWord(String filePath) throws IOException {

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.skip(new Random().nextInt(5757)).findFirst().get().split(" ")[0];
        }
    }

    public static void main(String[] args) throws Exception {
        // best initial guess: arose

        String word = getRandomWord("src/wordle/word_frequency_plurality_list");

        WordlePlayer solver = new WordlePlayer(word, "src/wordle/word_frequency_plurality_list");

        int numGuesses = 0;
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Enter initial guess:");
        String currentGuess = inputScanner.nextLine();
        numGuesses++;

        while (numGuesses <= MAX_GESSES) {
            if (word.equals(currentGuess)) {
                System.out.println("You got it! It took you " + numGuesses + " guesses.");
                System.exit(0);
            } else {
                solver.checkWord(currentGuess);
            }

            String userInput = inputScanner.nextLine();
            if (userInput.equals("--help")) {
                solver.printPossibilities();
            } else {
                currentGuess = userInput;
                numGuesses++;

            }
        }

        System.out.println("You have run out of guesses. Better luck next time.");


    }
}
