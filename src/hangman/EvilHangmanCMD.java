package hangman;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;


public class EvilHangmanCMD {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Input not provided");
            return;
        }

        String dictionaryFileName = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);


        boolean guessing;
        String word = "";

        for (int i = 0; i < wordLength; ++i) {
            word += '-';
        }

        StringBuilder finalGuess = new StringBuilder(word);
        //finalGuess.setCharAt(word.length()-1,',');

        char curChar;

        // Dictionary name
        File inputFile = new File(dictionaryFileName);

        EvilHangmanGame game = new EvilHangmanGame();

        //starts game
        //add loop here to input new dictionary name maybe
        try {
            game.startGame(inputFile, wordLength);
        }
        catch (IOException e) {
            System.out.println("IO Exception");
            System.out.println(e.getMessage()); // what if it doesn't return a message? - returns null
            e.printStackTrace();
            return;
        }
        catch (EmptyDictionaryException e) {
            System.out.println("Empty Dictionary");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        Scanner input = new Scanner(System.in);

        for (int i = numGuesses; i > 0; --i) {
            guessing = true;

            System.out.printf("You have %d guesses left%n", i);

            System.out.print("Used letters: ");
            if (game.getGuessedLetters() != null) {
                System.out.println(game.getGuessedLetters().toString());
            }

            System.out.print("Word: ");
            System.out.println(finalGuess.toString());

            while (guessing) {

                System.out.println("Enter guess: ");
                String inputString = input.next();

                if (inputString.length() == 0) {
                    System.out.println("Invalid Input!");
                }
                curChar = inputString.charAt(0);
                if (Character.isLetter(curChar)){
                    curChar = Character.toLowerCase(curChar);
                    guessing = false;

                    try {
                        Set<String> value;
                        value = game.makeGuess(curChar);

                        String ID = game.getFinalSetID();

                        int count = 0;
                        for (int k = 0; k < ID.length(); ++k) {
                            if (ID.charAt(k) != '-') {
                                finalGuess.setCharAt(k, ID.charAt(k));
                                count++;
                            }
                        }

                        if (count == 0) {
                            System.out.println("Sorry there are no " + curChar + "'s");
                        }
                        else if (count > 0) {
                            System.out.println("Yes! There are " + count + " " + curChar + "'s");
                            ++i;
                        }
                        //System.out.println(value.size());


                        if (value.size() == 1)  {
                            Iterator<String> itr = value.iterator();
                            //System.out.println(itr.next());
                            if (finalGuess.toString().equals(itr.next())) {
                                System.out.println("You Win! You guessed the word: " + finalGuess.toString());
                                return;
                            }
                        }
                        if (i == 1) {
                            Iterator<String> itr = value.iterator();

                            System.out.println("You lost, Word was: " + itr.next());

                        }
                    }
                    catch (GuessAlreadyMadeException e) {
                        System.out.println("Guess already made!");
                        guessing = true;
                    }

                }
                else {
                    System.out.println("Invalid Input!");
                }

            }
            System.out.println();





        }
    }

}
