package Actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FrequencyCount {
private static	Map<String, Integer> fileFrequencyMap = new HashMap<String,Integer>();
	// it counts the frequency of the word, if we type any product.
	private static void recurrenceOfWord(File file, Map<String, Map<String, Integer>> wordFrequencyMap,
			String searchWord) {
		try (Scanner scanner = new Scanner(file)) {// it reads each word from the file
			// it returns true if there is another word to search, next(move onto another)
			while (scanner.hasNext()) {
				// it converts words into lower case because user/customer won't care
				// while searching the product
				String wordSearch = scanner.next().replaceAll("[^a-zA-Z]", "").toLowerCase();
				// if word is equal to user/customer type word then it extracts filname using
				// get method.
				if (wordSearch.equals(searchWord.toLowerCase())) {
					String fileName = file.getName();

					// It extracts the map frequency you search, if map doesn't exist then
					// new empty map create and
					// also put method update the frequency count in the current file
					Map<String, Integer> fileFrequencyMap = wordFrequencyMap.getOrDefault(searchWord, new HashMap<>());
					fileFrequencyMap.put(fileName, fileFrequencyMap.getOrDefault(fileName, 0) + 1);
					wordFrequencyMap.put(searchWord, fileFrequencyMap);
				}
			}
		} catch (FileNotFoundException e) {// IOException
			e.printStackTrace();
		}
	}

	// It is for traverse all the files and
	// sub-files in the given folder and count the frequency of the search word in
	// each file.
	private static void searchingFolder(File folder, Map<String, Map<String, Integer>> wordFrequencyMap,
			String searchWord) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				// It is recursively traverse a main folder and its files(inside folder) to
				// count the frequency
				// of a specific word in each file and store the results in a map.
				searchingFolder(file, wordFrequencyMap, searchWord);
			} else {
				recurrenceOfWord(file, wordFrequencyMap, searchWord);
			}
		}
	}

	// i have use this because I don't want extension in the text files(output)
	// it searches for dot index from the last character from the file
	private static String getFileNameWithoutExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		// if the dot found in the file from the last,
		// then it will return the filename,
		if (dotIndex == -1) {
			return fileName;
			// if not found then it will check from the start until the dot index found and
			// then it will extract
		} else {
			return fileName.substring(0, dotIndex);
		}
	}

	public static int Frequency(String product) {

		String folderList[] = { "SaveOnFoodsHtml", "ShopFoodExHtml", "YupikHtml" };
		// It store the frequency of each word in each file in the folder such as
		// collections and yupik.
		Map<String, Map<String, Integer>> countFrequency = new HashMap<>();
		// it starts the process of traversing the folder and counting the frequency of
		// each word after that new file represents the folder to be traversed,
		// in the word frequency, it stores the word frequencies,
		// and what user want to search in the search word.
		for (String folderPath : folderList) {
			searchingFolder(new File(folderPath), countFrequency, product);
		}
		boolean wordFound = false; // initially bool is false,keep track of whether or
		// not the search word was found in any of the files.
		int count = 0;
		for (String word : countFrequency.keySet()) {
			// it checks if the current word is equal to the
			// word i was search, string using the equals method for this.
			if (word.equals(product)) {
				// if it is true, it extracts the value of the adjacent key in count frequency
				// (inside if statement is executed)
				wordFound = true;
				fileFrequencyMap = countFrequency.get(word);
				for (String fileName : fileFrequencyMap.keySet()) {
					int frequency = fileFrequencyMap.get(fileName);
					// for print the output, i used get file without extension etc
//					System.out.printf("In " + "%s, Frequency of %s is: %d\n", getFileNameWithoutExtension(fileName),
//							product, frequency);
					count += frequency;
				}
			}
		} // if the word you are type/search is not(!) found in both the files,
			// it will simply print word not found
		if (!wordFound) {
			System.out.println("Word not found.");
		}
		return count;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); // System.in is a standard input stream
		System.out.println("Enter product");
		String c = sc.nextLine();
		FrequencyCount.Frequency(c);
		for (String fileName : fileFrequencyMap.keySet()) {
			int frequency = fileFrequencyMap.get(fileName);
			 
			System.out.printf("In " + "%s, Frequency of %s is: %d\n", getFileNameWithoutExtension(fileName),
					c, frequency);
			
		}
	}
}
