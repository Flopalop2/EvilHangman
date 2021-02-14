package hangman;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


public class EvilHangman {

    public static void main(String[] args) {
        int wordLength = 0;
        int numGuesses = 0;
        String dictionaryFileName;
        String lengthString; //initializing null is redundant
        String guessesString;
        boolean isNumber;

        //make input method

        //dictionary name
        do {
            dictionaryFileName = JOptionPane.showInputDialog(
                    null,
                    "Please input the dictionary txt name: ",
                    "Dictionary",
                    JOptionPane.QUESTION_MESSAGE);

            //make method for this
            int sure = JOptionPane.NO_OPTION;
            if (dictionaryFileName == null) {
                sure = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to cancel?",
                        "Cancel",
                        JOptionPane.YES_NO_OPTION);
            }

            if (sure == JOptionPane.YES_OPTION) {
                return;
            }

        } while (dictionaryFileName == null || dictionaryFileName.equals(""));
        if (dictionaryFileName.equals("exit")) {
            return;
        }

        // length of string
        do {
            isNumber = true;
            lengthString = JOptionPane.showInputDialog(
                    null,
                    "Please input length of word: ",
                    "WordLength",
                    JOptionPane.QUESTION_MESSAGE);

            int sure = JOptionPane.NO_OPTION;
            if (lengthString == null) {
                sure = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to cancel?",
                        "Cancel",
                        JOptionPane.YES_NO_OPTION);
            }

            if (sure == JOptionPane.YES_OPTION) {
                return;
            }

            if (lengthString != null && lengthString.equals("exit")) {
                return;
            }

            try {
                //assert lengthString != null;
                if (lengthString != null) {
                    wordLength = Integer.parseInt(lengthString);
                }
            }
            catch (NumberFormatException ex) {
                isNumber = false;
            }
        } while (lengthString == null || !isNumber);

        // number of guesses
        do {
            isNumber = true;
            guessesString = JOptionPane.showInputDialog(
                        null,
                        "Please input max number of guesses: ",
                        "NumGuesses",
                        JOptionPane.QUESTION_MESSAGE);

            int sure = JOptionPane.NO_OPTION;
            if (guessesString == null) {
                sure = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to cancel?",
                        "Cancel",
                        JOptionPane.YES_NO_OPTION);
            }

            if (sure == JOptionPane.YES_OPTION) {
                return;
            }

            if (guessesString != null && guessesString.equals("exit")) {
                return;
            }

            try {
                if (guessesString != null) {
                    numGuesses = Integer.parseInt(guessesString);
                }
            }
            catch (NumberFormatException ex) {
                isNumber = false;
            }
        } while (guessesString == null || !isNumber);


        boolean guessing;

        //StringBuilder word = new StringBuilder();

        //word.append("-".repeat(Math.max(0, wordLength)));
        /*
        for (int i = 0; i < wordLength; ++i) {
            word.append('-');
        }
         */

        StringBuilder finalGuess = new StringBuilder("-".repeat(Math.max(0, wordLength))
 /*
 for (int i = 0; i < wordLength; ++i) {
     word.append('-');
 }
  */);
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
            JOptionPane.showMessageDialog(null, "IO Exception (dictionary not valid)", "Sup",JOptionPane.ERROR_MESSAGE);
            System.out.println("IO Exception");
            System.out.println(e.getMessage()); // what if it doesn't return a message? - returns null
            e.printStackTrace();
            return;
        }
        catch (EmptyDictionaryException e) {
            JOptionPane.showMessageDialog(null, "Empty Dictionary or no words of said length", "Sup",JOptionPane.WARNING_MESSAGE);
            System.out.println("Empty Dictionary");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }


        for (int i = numGuesses; i > 0; --i) {
            guessing = true;

            StringBuilder output = new StringBuilder();

            String toAppend = "You have " + i + " guesses left\n";
            output.append(toAppend);

            //System.out.printf("You have %d guesses left%n", i);

            output.append("Used letters: ");
            if (game.getGuessedLetters() != null) {
                output.append(game.getGuessedLetters().toString());
                output.append("\n");
            }

            output.append("Word: ");
            output.append(finalGuess.toString());
            output.append("\n");

            output.append("Enter guess: \n");

            while (guessing) {

                String inputString;
                do {
                    inputString = JOptionPane.showInputDialog(null, output, "Hey",JOptionPane.QUESTION_MESSAGE);

                    int sure = JOptionPane.NO_OPTION;
                    if (inputString == null) {
                        sure = JOptionPane.showConfirmDialog(
                                null,
                                "Are you sure you want to cancel?",
                                "Cancel",
                                JOptionPane.YES_NO_OPTION);
                    }

                    if (sure == JOptionPane.YES_OPTION) {
                        return;
                    }

                    if (inputString != null && inputString.equals("exit")) {
                        return;
                    }
                } while (inputString == null || inputString.isEmpty());


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
                            JOptionPane.showMessageDialog(null, "Sorry there are no " + curChar + "'s", "Shucks",JOptionPane.PLAIN_MESSAGE);
                        }
                        else if (count > 0) {
                            JOptionPane.showMessageDialog(null, "Yes! There are " + count + " " + curChar + "'s", "Sick",JOptionPane.INFORMATION_MESSAGE);
                            ++i;
                        }


                        if (value.size() == 1)  {
                            Iterator<String> itr = value.iterator();
                            //System.out.println(itr.next());
                            if (finalGuess.toString().equals(itr.next())) {
                                JOptionPane.showMessageDialog(null, "You Win! You guessed the word: " + finalGuess.toString(), "Nice",JOptionPane.PLAIN_MESSAGE);
                                //add option to replay? run main()
                                return;
                            }
                        }
                        if (i == 1) {
                            Iterator<String> itr = value.iterator();

                            JOptionPane.showMessageDialog(null, "You lost, Word was: " + itr.next(), "Dang",JOptionPane.PLAIN_MESSAGE);

                        }
                    }
                    catch (GuessAlreadyMadeException e) {
                        JOptionPane.showMessageDialog(null, "Guess already made!", "Oops",JOptionPane.ERROR_MESSAGE);
                        guessing = true;
                    }

                }
                else {
                    JOptionPane.showMessageDialog(null, "Invalid Input!", "Nope",JOptionPane.ERROR_MESSAGE);
                }

            }
            //
            // System.out.println();





        }
    }

}
