package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    private int curWordLength;
    private File dictionaryFile;
    private SortedSet<Character> guessedLetters;
    private Set<String> dictionarySet;
    private Set<String> subSet;
    private String finalSetID;

    public int getCurWordLength() {
        return curWordLength;
    }

    public void setCurWordLength(int curWordLength) {
        this.curWordLength = curWordLength;
    }

    public File getDictionaryFile() {
        return dictionaryFile;
    }

    public void setDictionaryFile(File dictionaryFile) {
        this.dictionaryFile = dictionaryFile;
    }

    //constructor
    public EvilHangmanGame() {
        setCurWordLength(0);
        setDictionaryFile(null);
        guessedLetters = new TreeSet<>();
        dictionarySet = new HashSet<>();
        subSet = new LinkedHashSet<>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        setCurWordLength(0);
        setDictionaryFile(null);
        guessedLetters = new TreeSet<>();
        dictionarySet = new HashSet<>();
        subSet = new LinkedHashSet<>();
        // file exists?
        if (!dictionary.exists()) { // same as saying (dictionary == null)
            throw new IOException();
        }

        // is dictionary empty?
        if (dictionary.length() == 0) {
            throw new EmptyDictionaryException();
        }

        setDictionaryFile(dictionary);
        setCurWordLength(wordLength);

        try (Scanner scanner = new Scanner(dictionary)) {
            //creates dictionary set
            while (scanner.hasNext()) {
                dictionarySet.add(scanner.next());
            }
            // subset of strings length wordLength
            /*
            Iterator<String> it = dictionarySet.iterator();
            while(it.hasNext()){
             */
            for (String curString : dictionarySet) {
                if (curString.length() == wordLength) {
                    subSet.add(curString);
                }
            }
            if (subSet.size() == 0) {
                throw new EmptyDictionaryException();
            }

            //testing
            /*
            Iterator<String> it2 = subSet.iterator();
            while(it2.hasNext()){
                System.out.println(it2.next());
            }
             */

        }
        catch (EmptyDictionaryException ex) {
            throw new EmptyDictionaryException("No Words of that length");
        }
        catch (Exception ex) {
            throw new IOException();
        }


    }

    public void setGuessedLetters(SortedSet<Character> guessedLetters) {
        this.guessedLetters = guessedLetters;
    }

    public Set<String> getDictionarySet() {
        return dictionarySet;
    }

    public void setDictionarySet(Set<String> dictionarySet) {
        this.dictionarySet = dictionarySet;
    }

    public Set<String> getSubSet() {
        return subSet;
    }

    public void setSubSet(Set<String> subSet) {
        this.subSet = subSet;
    }

    public String getFinalSetID() {
        return finalSetID;
    }

    public void setFinalSetID(String finalSetID) {
        this.finalSetID = finalSetID;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);

        if ((guessedLetters != null) && (guessedLetters.contains(guess))) {
            throw new GuessAlreadyMadeException();
        }
        else {
            assert guessedLetters != null; //safe to assert this?
            guessedLetters.add(guess);

            Set<Set<String>> setOfSets = new LinkedHashSet<>();

            StringBuilder curStringCopy = new StringBuilder();
            /*
            Iterator<String> it2 = subSet.iterator();
            while(it2.hasNext()){
             */
            for (String curString : subSet) {
                curStringCopy = new StringBuilder(curString);

                for (int i = 0; i < curWordLength; ++i) {
                    if (curString.charAt(i) != guess) {
                        curStringCopy.setCharAt(i, '-'); // set chars not guess to -
                    }
                }

                Iterator<Set<String>> itr = setOfSets.iterator();

                if (!itr.hasNext()) {
                    Set<String> TempHashSet = new LinkedHashSet<>();
                    TempHashSet.add(curStringCopy.toString());
                    TempHashSet.add(curString);
                    setOfSets.add(TempHashSet);

                }

                boolean found = false;
                while (itr.hasNext()) {
                    Set<String> curSet = itr.next();
                    Iterator<String> stringItr = curSet.iterator();
                    if (stringItr.next().equals(curStringCopy.toString())) {
                        found = true;
                        curSet.add(curString);
                    }
                }
                if (!found) {
                    Set<String> TempHashSet = new LinkedHashSet<>();
                    TempHashSet.add(curStringCopy.toString());
                    TempHashSet.add(curString);
                    setOfSets.add(TempHashSet);
                }
            }

            Set <String> finalSet = new LinkedHashSet<>(); //just make this subSet

            /*
            Iterator<Set<String>> setItr = setOfSets.iterator();
            while (setItr.hasNext()) {
             */
            for (Set<String> curSet : setOfSets) { //replaced with enhanced for loop
                //System.out.println(curSet);

                if (finalSet == null) {
                    finalSet = curSet;
                }
                else if (curSet.size() > finalSet.size()) {
                    finalSet = curSet;
                }
                else if (curSet.size() == finalSet.size()) {
                    Iterator<String> itr = curSet.iterator();
                    String curSetID = itr.next();

                    Iterator<String> itr2 = finalSet.iterator();
                    String finalSetID = itr2.next();

                    if (finalSetID.indexOf(guess) == -1) { //is finalID ----

                    }
                    else if (curSetID.indexOf(guess) == -1) { //is curID ----
                        finalSet = curSet;
                    }
                    else {
                        int curCount = 0;
                        int finalCount = 0;

                        for(int i=0; i < curSetID.length(); ++i) {
                            if(curSetID.charAt(i) == guess)
                                curCount++;
                            if(finalSetID.charAt(i) == guess)
                                finalCount++;
                        }

                        if (curCount == finalCount) {

                            for (int i = curSetID.length() - 1; i > 0; --i) {
                                if (curSetID.charAt(i) == guess) {
                                    if (finalSetID.charAt(i) != guess) {
                                        finalSet = curSet;
                                        break;
                                    }
                                }
                                else if (finalSetID.charAt(i) == guess){
                                    break;
                                }
                            }

                        }
                        else if (curCount < finalCount) {
                            finalSet = curSet;
                        }

                    }
                }
            }


            Iterator<String> itr2 = finalSet.iterator();
            finalSetID = itr2.next();

            subSet = finalSet; // meant to just have one set here.
            Iterator<String> itr = subSet.iterator();
            itr.next();
            itr.remove();

            //System.out.println(finalSet);
            return finalSet;
        }
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
