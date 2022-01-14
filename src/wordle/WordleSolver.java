package wordle;


import com.google.common.base.MoreObjects;
import trie.Trie;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordleSolver {
    private final static int MAX_GESSES = 6;
    private final static int WORD_LENGTH = 5;

    private final StringBuilder currentState;
    private final String word;
    private final Trie trie;
    private final Map<Character, Integer> charCountMap;
    private final Map<Character, Set<Integer>> charGuessesMap;
    private final SortedSet<Character> charsInWrongSpot;
    private final Set<Character> charsNotToAddToWrongSpotSet;
    private final SortedSet<Character> charsNotInWordSet;


    public WordleSolver(String word, Trie trie) {
        validateWord(word, trie);
        this.word = word;
        this.trie = trie;
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

    public void checkWord(String guess) {
        validateWord(guess, trie);

        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            //updateCharGuessesMap(i, guessChar);
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


        System.out.println(String.format("%s ... Wrong Spot: %s Not Present: %s",
                currentState.toString(), charsInWrongSpot.toString(),
                charsNotInWordSet.toString()));
    }

    static class SortableTuple implements Comparable<SortableTuple>{
        final String word;
        final int frequency;

        public SortableTuple(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SortableTuple)) return false;
            SortableTuple that = (SortableTuple) o;
            return frequency == that.frequency &&
                    Objects.equals(word, that.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, frequency);
        }

        @Override
        public int compareTo(SortableTuple o) {
            return Integer.compare(frequency, o.frequency);
        }

        @Override
        public String toString() {
            return word;
        }
    }

    public static void main(String[] args) throws Exception {
        // best initial guess: arose

        Trie trie = new Trie();

        Random rand = new Random();
        int random = rand.nextInt(5757);

        Scanner fileScanner = new Scanner(new File("src/wordle/words_list"));
        int currentWordNumber = 0;
        String word = "";

        int[] arr = new int[26];
        System.out.println(arr[0]);
        double totalChars = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();

            for (int i = 0; i < line.length(); i++) {
                arr[line.charAt(i) - 'a']++;
                totalChars++;
            }
            trie.addWord(line);
            if (currentWordNumber == random) {
                word = line;
            }
            currentWordNumber++;

        }

        word = "chill";
        final HashSet<Character> missingChars = Stream.of('r', 'o', 's', 'e', 'i', 'd', 'l', 'c', 'k')
                .collect(Collectors.toCollection(HashSet::new));
        final HashSet<Character> wrongSlotChars = Stream.of('a')
                .collect(Collectors.toCollection(HashSet::new));
        final Set<String> potentialWords = trie.generatePotentialWords("ta---", wrongSlotChars,
                missingChars);

        HashMap<String, Integer> frequencyHashMap = new HashMap<>();
        Scanner freqScanner = new Scanner(new File("src/wordle/5_letter_freq_unsorted"));
        while(freqScanner.hasNextLine()) {
            String[] array = freqScanner.nextLine().trim().split("  ");
            frequencyHashMap.put(array[0], Integer.parseInt(array[1]));

        }
        SortedSet<SortableTuple> sortableTuples = new TreeSet<>();
        for(String str : potentialWords) {
            final Integer frequencyValue = frequencyHashMap.get(str);
            sortableTuples.add(new SortableTuple(str, frequencyValue == null ? Integer.MIN_VALUE : frequencyValue));
        }
        System.out.println(sortableTuples);
        System.out.println(potentialWords);



        System.exit(0);
        WordleSolver solver = new WordleSolver(word, trie);

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
                // Not supported yet
                System.exit(0);
            } else {
                currentGuess = userInput;
                numGuesses++;

            }


        }

        System.out.println("You have run out of guesses. Better luck next time.");

    }
}
