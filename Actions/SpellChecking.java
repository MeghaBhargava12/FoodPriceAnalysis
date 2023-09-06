package Actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SpellChecking {
	
	/**
	 * This class represents a Spell Checking utility that checks if a given word is in a dictionary
	 * constructed from a set of text files.
	 * 
	 * The dictionary is stored as a trie data structure for efficient lookups.
	 * 
	 * @author Megha Bhargava
	 */
	// Define a constant for the size of the alphabet
	private static final int ALPHABET_SIZE = 26;
	
	// Declare a StringBuffer object to store a string
	StringBuffer s;

	// Create a Trie Node
	private static class TrieNode {
		TrieNode[] children = new TrieNode[ALPHABET_SIZE];
		boolean isEndOfWord = false;
	}

	// Create the root of the Trie
	private static TrieNode root = new TrieNode();

	// Insert a word into the Trie
	public static void insert(String word) {
		TrieNode node = root;
		// For each character in the word
		for (int i = 0; i < word.length(); i++) {
			// Get the character
			char c = word.charAt(i);
			// If it's not a lowercase letter, skip it
			if (c < 'a' || c > 'z') {
				continue;
			}
			// Compute the index of the character in the children array
			int index = c - 'a';
			// If the child node for the character does not exist, create it
			if (node.children[index] == null) {
				node.children[index] = new TrieNode();
			}
			// Move to the child node for the character
			node = node.children[index];
		}
		// Mark the node as the end of a valid word
		node.isEndOfWord = true;
	}
	
	// Search for a word in the Trie
	public static boolean search(String word) {
		TrieNode node = root;
		// For each character in the word
		for (int i = 0; i < word.length(); i++) {
			// Compute the index of the character in the children array
			int index = word.charAt(i) - 'a';
			// If the child node for the character does not exist, the word is not in the trie
			if (node.children[index] == null) {
				return false;
			}
			// Move to the child node for the character
			node = node.children[index];
		}
		// If we reach a node that represents the end of a valid word, the word is in the trie
		return node != null && node.isEndOfWord;
	}

	// Check if a given product name is spelled correctly
	public static boolean SpellChecker(String product) throws IOException {
		// TODO Auto-generated method stub
		String filesList[] = { "saveonfoods.txt", "shopfoodex.txt", "yupik.txt" };

		for (String fileName : filesList) {
			File f = new File(fileName);
			BufferedReader brd = new BufferedReader(new FileReader(f));
			String line;
			while ((line = brd.readLine()) != null) {
				for (String word : line.split("\\W+")) {
					if (!word.isEmpty()) {
						insert(word.toLowerCase());
					}
				}
			}
			brd.close();

		}
		// Check if a given product name is spelled correctly
		return search(product);
	}

}
