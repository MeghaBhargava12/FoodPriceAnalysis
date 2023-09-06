package Actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class InvertedIndexing<Value> {
	private static int N; // size of TST
	private Node rt; // root Node
	private boolean is_exit;
	private static int maximum = 0;
	private static ArrayList<Hashtable<Integer, Integer>> arr = new ArrayList<Hashtable<Integer, Integer>>();
	private static int document_index;
	private static Hashtable<Integer, String> webpage_list = new Hashtable<Integer, String>();

	// Defining the Structure of Node
	private class Node {
		private char a;
		private Node leftNode, middleNode, rightNode;
		private Value value; // value of the string
	}

	public int size() {
		return N;
	}

	// Validating that String is there or not
	public boolean contains(String k) {
		return get(k) != null;
	}

	private Value get(String k) {
		if (k == null)
			throw new NullPointerException(); // throws exception if string has length 0

		if (k.length() == 0)
			throw new IllegalArgumentException("Word must have be greater than one letter"); // throws exception if
																								// string is null
		Node x = get(rt, k, 0);
		if (x == null)
			return null;
		return x.value;

	}

	private Node get(Node x, String k, int b) {
		if (k == null)
			throw new NullPointerException();
		if (k.length() == 0)
			throw new IllegalArgumentException("Word must have be greater than one letter");
		if (x == null)
			return null;
		char a = k.charAt(b);
		if (a < x.a)
			return get(x.leftNode, k, b);
		else if (a > x.a)
			return get(x.rightNode, k, b);
		else if (b < k.length() - 1)
			return get(x.middleNode, k, b + 1);
		else
			return x;
	}

	// inserting string and its value
	public int put(String c, Value value) {
		if (!contains(c)) {
			N++;
			rt = put(rt, c, value, 0);
			return 0;
		} else
			return 1;
	}

	private Node put(Node x, String e, Value value, int d) {
		char c = e.charAt(d);
		if (x == null) {
			x = new Node();
			x.a = c;

		}
		if (c < x.a)
			x.leftNode = put(x.leftNode, e, value, d);
		else if (c > x.a)
			x.rightNode = put(x.rightNode, e, value, d);
		else if (d < e.length() - 1) {
			x.middleNode = put(x.middleNode, e, value, d + 1);
		} else {
			x.value = value;
		}
		return x;
	}

	// Evaluating Frequency Count
	private void tokenizestring(InvertedIndexing<Integer> st)

	{
		String folderList[] = { "SaveOnFoodsHtml", "ShopFoodExHtml", "YupikHtml" };
		for (String folder : folderList) {
			File webPageFolder = new File(folder);
			document_index = -1;
			String[] words = null;
			for (File input : webPageFolder.listFiles()) {

				String text = "";
				if (!input.isHidden()) {
					try {
						document_index++;
						webpage_list.put(document_index, input.getName());
						Document document = Jsoup.parse(input, "UTF-8");

						text = text + document.body().text();

					}

					catch (Exception e) {
						continue;
					}
					text = text.toLowerCase();
					words = text.split(" ");

					int i = 0;
					for (i = 0; i < words.length; i++) {

						if (st.get(words[i]) == null) {
							arr.add(new Hashtable<Integer, Integer>());
							st.put(words[i], N);
							boolean is_exist = false;
						}
						int wordindex = st.get(words[i]);
						try {
							if (wordindex <= arr.size()) {
								int freq = arr.get(wordindex).get(document_index);

								arr.get(wordindex).put(document_index, freq + 1);
							}
						} catch (Exception e) {
							try {
								arr.get(wordindex).put(document_index, 1);
							} catch (Exception e2) {
								System.out.println(e2);
							}
						}
					}
					if (i > maximum)
						maximum = i;
				}
			}

		}
	}

	public static void printFrequency(int index, Hashtable<String, String> url_Map) {

		for (int i = 0; i <= document_index; i++) {
			String filename = webpage_list.get(i);
			String url = url_Map.get(filename);
			int frequency = arr.get(index).get(i) == null ? 0 : arr.get(index).get(i);
			if (frequency > 0 & url != null) {
				System.out.println(url + "\t: " + frequency);
			}

		}

	}

	public static void Indexing(Hashtable<String, String> url_Map, String product) {
		InvertedIndexing<Integer> st = new InvertedIndexing<Integer>();
		st.tokenizestring(st);

		int index = 0;
		if (st.get(product) == null) {
			index = st.get(product);
		} else {
			index = st.get(product);

		}
		printFrequency(index, url_Map);// providing frequency according to the word in that url
		System.out.println();
	}

}
