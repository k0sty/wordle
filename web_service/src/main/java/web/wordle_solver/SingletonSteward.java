package web.wordle_solver;

import trie.Trie;

import java.io.FileNotFoundException;

public class SingletonSteward {

    // The purpose of this class is to be a singleton,
    // containing instances of classes used to facilitate wordle_solver
    // in order that the web_service does not repeat class instantiation
    // unecessarily

   //create an object of SingletonSteward
   private static SingletonSteward instance = new SingletonSteward();

   public Trie trie;

   //make the constructor private so that this class cannot be
   //instantiated
   private SingletonSteward(){
       try {
           trie = Trie.fromFile("word_frequency_plurality_list");
       } catch (FileNotFoundException e) {
           System.out.println(e);
       }
   }

   //Get the only object available
   public static SingletonSteward getInstance(){
      return instance;
   }

}