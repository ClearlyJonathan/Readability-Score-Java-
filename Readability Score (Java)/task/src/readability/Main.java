package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String pathToFile;
        File file;
        if (args.length > 0) {
            pathToFile = args[0];
        } else {
            pathToFile = "";
        }
        file = new File(pathToFile);

       try (Scanner scanner = new Scanner(file)) {
            String text = scanner.nextLine();
            printText(text);
            int charsCount = calculateCharactersCount(text);
            int wordsCount = calculateWordCount(text);
            int sentenceCount = calculateSentenceCount(text);
            int syllableCount =  calculateSyllableCount(text);
            int polysyllableCount = calculatePoliSyllable(text);
            System.out.printf("Words: %d\nSentences: %d\nCharacters: %d\nSyllables: %d\nPolysyllables: %d\n", wordsCount, sentenceCount, charsCount, syllableCount, polysyllableCount );

           System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
           Scanner optReader = new Scanner(System.in);
           String option = optReader.nextLine();
           switch (option) {
               case "ARI":
                   automatedReadabilityTest(text);
                   break;
               case "FK":
                    fleschKincaidReadability(text);
                    break;
               case "SMOG":
                   smog(text);
                   break;
               case "CL":
                   colemanIndex(text);
                   break;
               case "all":
                   automatedReadabilityTest(text);
                   fleschKincaidReadability(text);
                   smog(text);
                   colemanIndex(text);
                   break;
           }


        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }


    }

    static void automatedReadabilityTest(String text) {
        double score = outputDifficulty(text);
        int sc = 0;
        sc = (int) Math.ceil(score);
        int minAge, maxAge;
        if (sc != 14) {
            minAge = 5 + sc - 1;
            maxAge = 6 + sc - 1;
        } else {
            minAge = 18;
            maxAge = 22;
        }
        System.out.printf("Automated Readability Index: %d (about %d-year-olds)\n", sc, minAge);
    }
    static void fleschKincaidReadability(String text) {
        int charsCount = calculateCharactersCount(text);
        int wordsCount = calculateWordCount(text);
        int sentenceCount = calculateSentenceCount(text);
        int syllableCount =  calculateSyllableCount(text);
        double score = (0.39 * ( (double) wordsCount / sentenceCount)) + (11.8 * ( (double) syllableCount / wordsCount)) - 15.59;
        int sc = 0;
        sc = (int) Math.ceil(score);
        int minAge, maxAge;
        if (sc != 14) {
            minAge = 5 + sc - 1;
            maxAge = 6 + sc - 1;
        } else {
            minAge = 18;
            maxAge = 22;
        }
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", score, minAge);
    }
    static void smog(String text) {
        int polysyllableCount = calculatePoliSyllable(text);
        int sentenceCount = calculateSentenceCount(text);
        double score = 1.043 * (Math.sqrt((double) polysyllableCount * (30 / (double) sentenceCount))) + 3.1291;
        int sc = 0;
        sc = (int) Math.ceil(score);
        int minAge, maxAge;
        if (sc != 14) {
            minAge = 5 + sc - 1;
            maxAge = 6 + sc - 1;
        } else {
            minAge = 18;
            maxAge = 22;
        }
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", score, minAge);
    }
    static void colemanIndex(String text) {
        int charsCount = calculateCharactersCount(text);
        int wordsCount = calculateWordCount(text);
        int sentenceCount = calculateSentenceCount(text);
        double l =  (double) charsCount / wordsCount * 100;
        double s = (double) sentenceCount / wordsCount * 100;
        double score = 0.0588 * l - 0.296 * s - 15.8;
        int sc = 0;
        sc = (int) Math.ceil(score);
        int minAge, maxAge;
        if (sc != 14) {
            minAge = 5 + sc - 1;
            maxAge = 6 + sc - 1;
        } else {
            minAge = 18;
            maxAge = 22;
        }
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n", score, minAge);
    }


    static double outputDifficulty(String text) {
        int charsCount = calculateCharactersCount(text);
        int wordCount = calculateWordCount(text);
        int sentenceCount = calculateSentenceCount(text);
        return calculateGradeByFormula(charsCount, wordCount, sentenceCount);
    }
    static void printText(String text) {
        System.out.println("The text is: ");
        System.out.println(text);
    }

    static double calculateGradeByFormula(int characters, int words, int sentence) {
        final double CHARACTERS_PER_WORD = 4.71;
        final double WORDS_PER_SENTENCE = 0.5;
        final double T = 21.43;
        return  (CHARACTERS_PER_WORD * ((double) characters / words) + WORDS_PER_SENTENCE * ((double) words / sentence) - T);
    }
    static int calculateWordCount(String text) {
        String[] words = text.split("\\s+"); // One or more spaces(basically divides into words)
        return words.length;
    }
    static int calculateSentenceCount(String text) {
        String[] sentences = text.split("[!.?]");
        return sentences.length;
    }
    static int calculateCharactersCount(String text) {
        int count = 0;
        for (String word: text.split(" ")) {
            for (char c : word.toCharArray()) {
                count++;
            }
        }
        return count;
    }
    static int calculateSyllableCount(String text) {
        text = text.toUpperCase();
        int vowelCount = 0;
        char[] vowels = "AEIOUY".toCharArray();
        String[] words = text.split("\\s+" );
        for (int i = 0; i < words.length; i++) {
            boolean hasVowel = false;
            int currentVowelCount = 0;
            char[] currentWord = words[i].toCharArray();
            for (int j = 0; j < currentWord.length; j++) {
                if (String.valueOf(currentWord).equals("YOU")) {
                    // System.out.printf("I've found a 'you'\n");
                    break;
                }
                if (hasVowel) {
                    hasVowel = false;
                    continue;
                }
                if (j ==  currentWord.length - 1 && currentWord[j] == 'E') {
                    continue;
                }
                if (Arrays.toString(vowels).contains(Character.toString(currentWord[j])) && !hasVowel) {
                    // Vowel counter
                    hasVowel = true;
                    currentVowelCount++;
                }
            }
            if (currentVowelCount == 0 && currentWord[currentWord.length - 1] != 'E') {
                // System.out.printf("Triggered with %s\n", Arrays.toString(currentWord));
                currentVowelCount++;
            }
            vowelCount += currentVowelCount;
        }
        return vowelCount;
    }
    static int calculatePoliSyllable(String text) {
        int poliCount = 0;
        text = text.toUpperCase();
        char[] vowels = "AEIOUY".toCharArray();
        String[] words = text.replaceAll("[,!?.]", "").split("\\s+");
        for (int i = 0; i < words.length; i++) {
            boolean hasVowel = false;
            int currentVowelCount = 0;
            char[] currentWord = words[i].toCharArray();
            for (int j = 0; j < currentWord.length - 1; j++) {
                if (hasVowel) {
                    hasVowel = false;
                    continue;
                }
                if (j ==  currentWord.length - 1 && currentWord[j] == 'E') {
                    continue;
                }
                if (Arrays.toString(vowels).contains(Character.toString(currentWord[j])) && !hasVowel){
                    hasVowel = true;
                    currentVowelCount++;
                }
            }
            if (currentVowelCount > 2) {
                System.out.printf("Triggered with the word %s\n", Arrays.toString(currentWord));
                poliCount++;
            }
        }
        System.out.println(poliCount);
        return poliCount;
    }
}

