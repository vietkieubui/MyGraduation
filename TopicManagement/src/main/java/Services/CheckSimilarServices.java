/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author BVKieu
 */
public final class CheckSimilarServices {
    public static double calculateSimilar(String[] text1, String[] text2) {
        double result = 0;

        // Detect similarity using Winnowing algorithm
        for (int i = 1; i <= 3; i++) {
            double similarity = detectSimilarity(text1, text2, i);
            System.out.println("Result in k= " + i + ":" + similarity);
            result += similarity;
        }
        result /= 3;
        return result;
    }

    public static double detectSimilarity(String[] text1, String[] text2, int k) {
        List<String> kGrams1 = generateKGrams(text1, k);
        List<String> kGrams2 = generateKGrams(text2, k);

        Set<String> fingerprints1 = calculateFingerprints(kGrams1);
        Set<String> fingerprints2 = calculateFingerprints(kGrams2);

        double jaccardCoefficient = calculateJaccardCoefficient(fingerprints1, fingerprints2);

        return jaccardCoefficient;
    }

    public static List<String> generateKGrams(String[] text, int k) {
        List<String> kGrams = new ArrayList<>();

        for (int i = 0; i < text.length - k; i++) {
            String[] subTextArray = Arrays.copyOfRange(text, i, i + k);
            String subTextString = String.join(" ", subTextArray);
            kGrams.add(subTextString);
        }
        return kGrams;
    }

    public static Set<String> calculateFingerprints(List<String> kGrams) {
        Map<Integer, Integer> kGramHashes = new HashMap<>();
        Set<String> fingerprints = new HashSet<>();

        for (int i = 0; i < kGrams.size(); i++) {
            String kGram = kGrams.get(i);
            int kGramHash = kGram.hashCode();

            if (!kGramHashes.containsKey(i)) {
                kGramHashes.put(i, kGramHash);
            }
        }

        for (int i = 0; i < kGrams.size(); i++) {
            String kGram = kGrams.get(i);
            int kGramHash = kGramHashes.get(i);

            if (isMinima(kGramHash, kGramHashes, i)) {
                fingerprints.add(kGram);
                System.out.println(kGramHash);
            }
        }
//        System.out.println(fingerprints);
        return fingerprints;
    }

    public static boolean isMinima(int kGramHash, Map<Integer, Integer> kGramHashes, int index) {
        for (int i = index - 3; i <= index + 3; i++) {
            if (i >= 0 && i < kGramHashes.size() && kGramHashes.get(kGramHashes.keySet().toArray()[i]) < kGramHash) {
                return false;
            }
        }
        return true;
    }

    public static double calculateJaccardCoefficient(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        double jaccardCoefficient = (double) intersection.size() / union.size();

        return jaccardCoefficient;
    }

}
