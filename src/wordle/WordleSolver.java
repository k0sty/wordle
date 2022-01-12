package wordle;


import trie.Trie;

import java.io.File;
import java.util.Scanner;

public class WordleSolver {

    public static void main(String[] args) throws Exception {
        Trie trie = new Trie();

        Scanner scanner = new Scanner(new File("src/wordle/words_list"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            trie.addWord(line, 0);

        }
        trie.printAllWords();

    }
}
