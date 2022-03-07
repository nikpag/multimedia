package src.classes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.lang.Character;

public class Game {
    public Set<String> candidates;
    public String pickedWord;
    public String[] filledLetters;
    public ArrayList<ArrayList<Tuple>> letters;
    public int points;
    public int wrongChoices;
    public int correctChoices;
    public boolean win;
    public boolean lose;
    public boolean notYet;
    public Dictionary activeDictionary;

    public Game(Dictionary activeDictionary) {
        this.activeDictionary = activeDictionary;

        points = 0;
        wrongChoices = 0;
        win = false;
        lose = false;
        notYet = false;

        pickedWord = getRandomWordFromActiveDictionary();

        filledLetters = new String[pickedWord.length()];

        for (int i = 0; i < filledLetters.length; i++) {
            filledLetters[i] = "_";
        }

        candidates = new HashSet<>(activeDictionary.dictionary);

        sieveLength();

        letters = new ArrayList<>();

        for (int i = 0; i < pickedWord.length(); i++) {
            letters.add(new ArrayList<Tuple>());
        }

        /* initialize letter array with (C, P) tuples */
        for (ArrayList<Tuple> tupleList : letters) {
            for (Character charLetter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                tupleList.add(new Tuple(charLetter, 0f));
            }
        }

        calculateProbabilities();

        for (ArrayList<Tuple> tupleList : letters) {
            Collections.sort(tupleList, new TupleComparator());
        }
    }

    public void setActiveDictionary(Dictionary dictionary) {
        activeDictionary = dictionary;
    }

    public Dictionary getActiveDictionary() {
        return activeDictionary;
    }

    public void calculateProbabilities() {
        /* Calculate probabilities */
        int i = 0;
        for (ArrayList<Tuple> tupleList : letters) {
            for (Tuple tuple : tupleList) {
                int counter = 0;
                for (String word : candidates) {
                    if (tuple.character == word.charAt(i)) {
                        counter++;
                    }
                }
                tuple.probability = (float) counter / candidates.size();
            }
            i++;
        }
    }

    public void sieveLength() {
        Set<String> candidatesClone = new HashSet<>(candidates);
        /* Sieve based on length */
        for (String item : candidatesClone) {
            if (item.length() != pickedWord.length()) {
                candidates.remove(item);
            }
        }
    }

    public void sieveLetter(Character picked, int index, String mode) {
        Set<String> candidatesClone = new HashSet<>(candidates);
        for (String item : candidatesClone) {
            boolean conditionOfRemoval = false;
            switch (mode) {
            case "right":
                conditionOfRemoval = picked != item.charAt(index);
                break;
            case "wrong":
                conditionOfRemoval = picked == item.charAt(index);
                break;
            }
            if (conditionOfRemoval) {
                candidates.remove(item);
            }
        }
    }

    public String getRandomWordFromActiveDictionary() {
        Random rand = new Random();
        int wordIndex = rand.nextInt(activeDictionary.dictionary.size());
        pickedWord = activeDictionary.dictionary.get(wordIndex);
        return pickedWord;
    }

    public void correctChoice(char letter, int charIndex) {
        /* Correct choice */
        filledLetters[charIndex] = String.valueOf(letter);
        sieveLetter(letter, charIndex, "right");
        int indexOf = -1;
        int cnt = 0;
        for (Tuple tuple : letters.get(charIndex)) {
            if (tuple.character == letter) {
                indexOf = cnt;
            }
            cnt++;
        }
        float prob = letters.get(charIndex).get(indexOf).probability;
        int pointsToGet;
        if (prob >= 0.6) {
            pointsToGet = 5;
        }
        else if (prob >= 0.4 && prob < 0.6) {
            pointsToGet = 10;
        }
        else if (prob >= 0.25 && prob < 0.4) {
            pointsToGet = 15;
        }
        else {
            pointsToGet = 30;
        }
        points += pointsToGet;
        correctChoices++;
    }

    public void wrongChoice(char letter, int charIndex) {
        sieveLetter(letter, charIndex, "wrong");
        int pointsToLose = 15;
        points -= pointsToLose;
        if (points < 0) {
            points = 0;
        }
        wrongChoices++;
    }

    /* wrong choice and lose = -2, wrong choice = -1, nothing happened = 0, correct choice = 1, correct choice and win = 2 */
    public int makeChoice(char letter, int charIndex) throws IOException {
        /* if already won/lost, return the appropriate value */
        if (win) {
            return 2;
        }
        if (lose) {
            return -2;
        }
        int ret = 0;

        for (ArrayList<Tuple> tupleList : letters) {
            Collections.sort(tupleList, new TupleComparator());
        }
        notYet = false;

        if (letter == pickedWord.charAt(charIndex) && filledLetters[charIndex] == "_") {
            correctChoice(letter, charIndex);
            ret = 1;
        }
        else if (filledLetters[charIndex] == "_") { /* Wrong choice */
            wrongChoice(letter, charIndex);
            ret = -1;
        }
        calculateProbabilities();

        if (wrongChoices == 6) {
            lose = true;
            return -2; /* you have lost */
        }

        for (String l : filledLetters) {
            if (l == "_") {
                notYet = true;
                break;
            }
        }
        if (notYet) {
            return ret; /* not yet */
        }
        else {
            win = true;
            return 2; /* you have won */
        }
    }
}
