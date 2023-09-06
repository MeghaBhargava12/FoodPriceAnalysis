package Actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class SearchFrequency {
	private static HashMap<String, Integer> numbers = new HashMap<String, Integer>(); // Here we and put the alternative
	private static HashMap<String, Integer> searchword = new HashMap<String, Integer>();

	public static void SearchCount(String product) throws Exception {

		String fileList[] = { "saveonfoods.txt", "shopfoodex.txt", "yupik.txt" };
		// Read all the lines in the file
		for (String file : fileList) {
			BufferedReader brd = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = brd.readLine()) != null) {
				sb.append(line);
			}
			String content = sb.toString();
			// split the words obtained and create array
			String[] words = content.split(" ");

			// put the word in dictonary
			for (String word : words) {
				word = word.replaceAll("[^a-zA-Z]+", "").toLowerCase();
				if (word != "") {
					if (numbers.containsKey(word)) {
						continue;
					} else {
						numbers.put(word, 0);
					}
				}
			}
			brd.close();
		}

		// Checking Word in Web page words Dictionary
		if (numbers.containsKey(product)) {
			// Validation it in search History Dictionary
			if (searchword.containsKey(product)) {
				int f = searchword.get(product);

				searchword.put(product, f + 1);// if yes increasing the search count
				System.out.println(searchword);
			} else {
				searchword.put(product, 1);// else putting it in dictionary
				System.out.println(searchword);
			}
		} else {
			System.out.println("The entered Word is not present in Webpage");
		}

	}

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in); // System.in is a standard input stream
		String c = "";
		while (c != "exit") {
			System.out.print("Enter a string: ");
			c = sc.nextLine();
			SearchFrequency.SearchCount(c);
		}
	}

}
