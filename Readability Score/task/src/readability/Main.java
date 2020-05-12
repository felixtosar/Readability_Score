package readability;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        long sentences = 0;
        long characters;
        long syllables = 0;
        long polysyllables = 0;
        String pathToFile = args[0];
        String line = readFile(pathToFile);
        sentences+= line.split("[!.?]").length;
        characters = line.replaceAll("\\s", "").length();

        String[] words = line.split("[(,.:;!?)]?\\s");
        long vowelsAux;
        long finalEVowels;
        long total;
        for (String word : words) {
            vowelsAux = regexCount(word, "[aeiouyAEIOUY]+");
            finalEVowels = regexCount(word, "e[(,.:;!?)]?$");
            total = vowelsAux - finalEVowels;

            if (total == 0) {
                syllables++;
            } else {
                syllables += total;
                if (total > 2) {
                    polysyllables++;
                }
            }
        }

        Map<Integer, String> ages = new HashMap<>();
        ages.put(1, "6");
        ages.put(2, "7");
        ages.put(3, "9");
        ages.put(4, "10");
        ages.put(5, "11");
        ages.put(6, "12");
        ages.put(7, "13");
        ages.put(8, "14");
        ages.put(9, "15");
        ages.put(10,"16");
        ages.put(11,"17");
        ages.put(12,"18");
        ages.put(13,"24");
        ages.put(14,"24+");

        System.out.println("The text is:\n" + line);
        System.out.println("\nWords: " + words.length);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        double score;
        switch(option) {
            case "ARI":
                score = automatedReadability(characters, words.length, sentences);
                System.out.printf("Automated Readability Index: %.2f (about %s year olds).\n", score, ages.get((int)score));
                break;
            case "FK":
                score = fleschKincaidReadability(words.length, sentences, syllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).\n", score, ages.get((int)score));
                break;
            case "SMOG":
                score = smogIndex(polysyllables, sentences);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s year olds).\n", score, ages.get((int)score));
                break;
            case "CL":
                score = colemanLiauIndex(characters, words.length, sentences);
                System.out.printf("Coleman–Liau index: %.2f (about %s year olds).\n", score, ages.get((int)score));
                break;
            case "all":
                long totalScore = 0;
                score = automatedReadability(characters, words.length, sentences);
                totalScore += score;
                System.out.printf("Automated Readability Index: %.2f (about %s year olds).\n", score, ages.get((int)score));
                score = fleschKincaidReadability(words.length, sentences, syllables);
                totalScore += score;
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).\n", score, ages.get((int)score));
                score = smogIndex(polysyllables, sentences);
                totalScore += score;
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s year olds).\n", score, ages.get((int)score));
                score = colemanLiauIndex(characters, words.length, sentences);
                totalScore += score;
                System.out.printf("Coleman–Liau index: %.2f (about %s year olds).\n", score, ages.get((int)score));
                System.out.println("This text should be understood in average by " + totalScore / 4 + " year olds.");
                break;
            default:
                System.out.println("Wrong option: " + option);
        }
    }

    private static double automatedReadability(double characters, double words, double sentences) {
        return 4.71 * characters / words + 0.5 * words / sentences - 21.43;
    }

    private static double fleschKincaidReadability(double words, double sentences, double syllables) {
        return 0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59;
    }

    private static double smogIndex(double polysyllables, double sentences) {
        return 1.043 * Math.sqrt(polysyllables * 30 / sentences) + 3.1291;
    }

    private static double colemanLiauIndex(double characters, double words, double sentences) {
        double l = characters * 100 / words; // average number of characters per 100 words
        double s = sentences * 100 / words; // average number of sentences per 100 words
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    private static long regexCount(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.results().count();
    }

    private static String readFile(String pathToFile) {
        String line = "";
        File file = new File(pathToFile);
        try (Scanner scannerFile = new Scanner(file)) {
            while (scannerFile.hasNext()) {
                line = scannerFile.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFile);
        }
        return line;
    }
}