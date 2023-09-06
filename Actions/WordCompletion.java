package Actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordCompletion {

	private static final int md = 2; // This constant will be used as a threshold to limit the Levenshtein distance
										// between two words that are considered similar.

	/**
	 * Calculates the Levenshtein distance between two strings. The Levenshtein
	 * distance also called the Edit distance, is the minimum number of operations
	 * required to transform one string to another.
	 */
	private static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}
		// initializing the lengthof strings

		int n = s.length();
		int m = t.length();

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		int[] d = new int[m + 1];
		// calculates the distance between strings and stores them in distance matrix
		// 'd'

		for (int j = 0; j <= m; j++) {
			d[j] = j;
		}

		for (int i = 1; i <= n; i++) {
			int prev = i;
			for (int j = 1; j <= m; j++) {
				int temp = d[j - 1];
				d[j - 1] = prev;
				prev = Math.min(Math.min(prev + 1, d[j] + 1), temp + (s.charAt(i - 1) == t.charAt(j - 1) ? 0 : 1));
			}
			d[m] = prev;
		}

		return d[m];
	}

	/**
	 * Reads all words from a file and returns them in a list.
	 */
	private static List<String> readWordsFromFile(String filename) {
		List<String> words = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] lineWords = line.split("\\s+");
				for (String word : lineWords) {
					words.add(word);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}

	/**
	 * Finds the most similar words to the given word in the specified files and web
	 * pages.
	 *
	 * @param words     the word to find similar words for
	 * @param filenames the names of the files to search for similar words in
	 *                  readWordsFromFile
	 * @return a list of the most similar words found
	 */
	public static List<String> findSimilarWords(String word) {
		List<String> filenames = Arrays.asList("yupik.txt", "saveonfoods.txt", "shopfoodex.txt");
		Set<String> words = new HashSet<>();
		for (String filename : filenames) {
			words.addAll(readWordsFromFile(filename)); // adding all words in filename to 'Words'
		}

		Map<String, Integer> distances = new HashMap<>();
		PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

		distances.put(word, 0);
		queue.offer(word);

		// implements the BFS in words and computes the distance

		while (!queue.isEmpty()) {
			String current = queue.poll();
			int dist = distances.get(current);

			if (dist > md) {
				break;
			}

			for (String w : words) {
				if (current.equals(w)) {
					continue;
				}
				int d = getLevenshteinDistance(current, w);
				if (d <= md) {
					int newDistance = dist + d;
					if (!distances.containsKey(w) || newDistance < distances.get(w)) {
						distances.put(w, newDistance);
						queue.offer(w);
					}
				}
			}
		}

		List<String> similarWords = new ArrayList<>();
		for (String w : distances.keySet()) {
			if (distances.get(w) <= md) {
				similarWords.add(w);
			}
		}
		// Sort the similar words by comparing the distance and gives the top 5
		// suggestions
		Collections.sort(similarWords, Comparator.comparingInt(w -> distances.get(w))); // sort by distance
		return similarWords.subList(0, Math.min(similarWords.size(), 5)); // return only top 5 words
	}

}
