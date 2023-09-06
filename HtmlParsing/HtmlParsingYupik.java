package HtmlParsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlParsingYupik {

	public static HashMap<String, HashMap<String, String>> Collectionof_foodAndPrice = new HashMap<String, HashMap<String, String>>();
	public static List<String> fileName = new ArrayList<String>();

	public static List<String> parse() throws IOException {

		// an instance of the current class, used to call methods
		HtmlParsingYupik htmlParser = new HtmlParsingYupik();

		// list all the HTML files inside the given folder
		File directory = new File("YupikHtml");
		File[] listOfFiles = directory.listFiles((dir, name) -> name.endsWith(".html"));

		// iterate on the HTML files and send each file to the parser method
		for (File file : listOfFiles) {
			if (file.isFile()) {
				htmlParser.parseFile(file.getAbsolutePath());
			}
		}
		return fileName;
	}

	// this method receives a HTML file full path and use Jsoup to parse the file
	public void parseFile(String url) throws IOException {
		// System.out.println("Startd parse");

		File inputFile = new File(url);
		// parse the file by use of Jsoup library
		Document doc = Jsoup.parse(inputFile, null);
		String cleanTitle = doc.title();
		String title = cleanTitle.replace("| Yupik", "").trim();
		// System.out.println(cleanTitle);

		List<Element> pi_list = doc.select("div.product-item-main> h1.product.name.product-item-name a");

		if (pi_list.size() == 0) {
			return;
		}
		Element pi_CollectionName = pi_list.get(0);
		String CollectionName = pi_CollectionName.text();
		// System.out.println(CollectionName);

		List<Element> pp_List = doc.select("span.price");

		if (pp_List.size() == 0) {
			return;
		}
		Element pp_CollectionName = pp_List.get(0);
		String CollectionPrice = pp_CollectionName.text();
		// check if the collection name exists in the hash map
		if (Collectionof_foodAndPrice.containsKey(CollectionName)) {
			// If the product already exists in the hashmap, do nothing

		} else {
			// Add the product name, price, and title to the hashmap
			HashMap<String, String> foodAndPrice = new HashMap<String, String>();
			foodAndPrice.put("price", CollectionPrice);
			foodAndPrice.put("title", title);
			Collectionof_foodAndPrice.put(CollectionName, foodAndPrice);
		}
		// write the product name, title and price to the output file
		String outputFileName = "yupik.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true));

		for (Map.Entry<String, HashMap<String, String>> product : Collectionof_foodAndPrice.entrySet()) {
			String productName = product.getKey();
			HashMap<String, String> foodAndPrice = product.getValue();
			String productPrice = foodAndPrice.get("price");
			String productTitle = foodAndPrice.get("title");
			String output = productTitle + " | " + productName + " | " + productPrice;
			if (!fileName.contains(output)) {
				fileName.add(output);
				writer.write(output);
				writer.newLine();
			}
		}
		writer.close();
	}
}