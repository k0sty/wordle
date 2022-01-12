package wordle;


import trie.Trie;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class WordleSolver {
    private final static int MAX_GESSES = 6;
    private final static int WORD_LENGTH = 5;

    private final String word;
    private final Trie trie;
    private final Set<Character> actualWordCharSet;
    private final SortedSet<Character> charsInWrongSpot;
    private final SortedSet<Character> charsNotInWordSet;


    public WordleSolver(String word, Trie trie) {
        validateWord(word, trie);
        this.word = word;
        this.trie = trie;
        actualWordCharSet = new HashSet<>();
        charsNotInWordSet = new TreeSet<>();
        charsInWrongSpot = new TreeSet<>();
        for (Character curr : word.toCharArray()) {
            actualWordCharSet.add(curr);
        }
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
        StringBuilder ret = new StringBuilder("-----");

        validateWord(guess, trie);

        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == word.charAt(i)) {
                ret.setCharAt(i, guessChar);
            } else if (actualWordCharSet.contains(guessChar)) {
                charsInWrongSpot.add(guessChar);
            } else {
                charsNotInWordSet.add(guessChar);
            }
        }

        System.out.println(String.format("%s %s %s", ret.toString(), charsInWrongSpot.toString(),
                charsNotInWordSet.toString()));
    }

    public static void main(String[] args) throws Exception {
        Trie trie = new Trie();

        Random rand = new Random();
        int random = rand.nextInt(5757);

        Scanner fileScanner = new Scanner(new File("src/wordle/words_list"));
        int currentWordNumber = 0;
        String word = "";
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            trie.addWord(line);
            if (currentWordNumber == random) {
                word = line;
            }
            currentWordNumber++;

        }
        System.out.println(word);
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
