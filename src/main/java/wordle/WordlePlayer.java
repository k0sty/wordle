package wordle;


import trie.Trie;
import trie.WordWrapper;
import utils.StreamResources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        this(word, Trie.fromFile(filePath));
    }

    public WordlePlayer(String word, Trie trie) {
        validateWord(word, trie);

        this.trie = trie;
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
        System.out.println(getPossibilities());
    }

    public SortedSet<WordWrapper> getPossibilities() {
        return trie.generatePotentialWords(currentState.toString(), charGuessesMap, charsNotInWordSet);
    }

    public void checkWord(String guess, boolean print) {

        validateWord(guess, trie);

        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == word.charAt(i)) {
                currentState.setCharAt(i, guessChar);
            } else if (charCountMap.containsKey(guessChar) &&
                    !charsNotToAddToWrongSpotSet.contains(guessChar)){
                charsInWrongSpot.add(guessChar);
                if (charGuessesMap.containsKey(guessChar)) {
                    charGuessesMap.get(guessChar).add(i) ;
                } else {
                    charGuessesMap.put(guessChar, Stream.of(i)
                            .collect(Collectors.toCollection(TreeSet::new)));
                }
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

        if (print) {
            System.out.printf("%s ... Wrong Spot: %s Not Present: %s Guess Map: %s%n",
                    currentState, charsInWrongSpot.toString(),
                    charsNotInWordSet.toString(), charGuessesMap);
        }
    }




    private static String getRandomWord(String filePath) throws IOException {

        final InputStream inputStream = StreamResources.getFileFromResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        int numLines = 5757;
        Random r = new Random();
        int desiredLine = r.nextInt(numLines);

        String theLine="";
        int lineCtr = 0;
        while ((theLine = br.readLine()) != null)   {
            if (lineCtr == desiredLine) {
                return theLine.trim().split(" ")[0];
            }
            lineCtr++;
        }

        throw new IllegalArgumentException("");
    }

    public static void main(String[] args) throws Exception {
        // best initial guess: arose

        //String word = getRandomWord("word_frequency_plurality_list");
        //System.out.println(word);
        String word = "heard";
        WordlePlayer solver = new WordlePlayer(word, "word_frequency_plurality_list");

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
                solver.checkWord(currentGuess, true);
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
