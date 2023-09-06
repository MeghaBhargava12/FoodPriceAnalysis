package Actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductCategoryList {
	private static final Map<String, String> CATEGORY_REGEXES = new HashMap<>();
	// Data files of each website
	private static String filesList[] = { "saveonfoods.txt", "shopfoodex.txt", "yupik.txt" };

	/**
	 * Map of different categories and their regex pattern
	 * 
	 * @author Megha Bhargava
	 */
	static {
		CATEGORY_REGEXES.put("baking", "baking|cake|decorating|mixes|baking soda|yeast|rising agents|bakery");
		CATEGORY_REGEXES.put("beverages", "juice|drink|sports drinks|tea|hot cocoa|cider mixes|soda");
		CATEGORY_REGEXES.put("candies and chocolates", "candies|candy|chocolate|bars");
		CATEGORY_REGEXES.put("canned and packaged foods", "canned|pasta|rice|couscous|quinoa|barley|soups");
		CATEGORY_REGEXES.put("condiments and toppings", "dressing|dips|cooking and topping|gravy|toppings");
		CATEGORY_REGEXES.put("dairy", "cheese|yogurt|sour cream|dips|evaporated|condensed|instant milks|milk");
		CATEGORY_REGEXES.put("desserts and sweets",
				"pudding|desserts|cakes|dessert kits|pies|tarts|pastries|bagels|muffins");
		CATEGORY_REGEXES.put("dried fruits",
				"dried fruits|raisins|apricots|cranberries|gingers|papayas|coconuts|tomatoes");
		CATEGORY_REGEXES.put("fish and seafood", "fish|shrimp|shell fish");
		CATEGORY_REGEXES.put("flours and grains",
				"grains|seeds|flours|roti|naan|buckwheat|millet|oat|rice mixes|chia seeds|rye");
		CATEGORY_REGEXES.put("fruits and vegetables", "fresh fruit|fresh vegetables");
		CATEGORY_REGEXES.put("gift cards and gifts", "gift cards|gifts");
		CATEGORY_REGEXES.put("meat and seafood",
				"meat|game|specialty|chicken|turkey|beef|veal|pork|ham|hot dogs|sausages|party platters|seafood|lamb");
		CATEGORY_REGEXES.put("nut and seeds", "nut|seed|nut butters|antioxidant");
		CATEGORY_REGEXES.put("oils and sprays", "oil|sprays|olive oils|cooking oils");
		CATEGORY_REGEXES.put("organic", "organic|plum organics");
		CATEGORY_REGEXES.put("spice, soups and pasta",
				"pasta|sauce|dough products|pancake mix|cereal bars|herbs|spices|seasonings|soup");
		CATEGORY_REGEXES.put("salads and greens", "salad kits|greens|salad toppings");
		CATEGORY_REGEXES.put("snacks", "snacks|coated nuts|fruit and veggie chips|corn snacks|sesame sticks|cookies");
		CATEGORY_REGEXES.put("pizza, breads and meals",
				"pizza|bread|breakfast|meal|pastries|sandwich|gravies|sides|instant");
		CATEGORY_REGEXES.put("baby and infants", "baby|infants");
	}

	/**
	 * Categorize a product by matching its name with category regular expressions
	 * 
	 * @author Megha Bhargava
	 * @param productName
	 * @return category of the product
	 */
	public static String categorizeProduct(String productName) {
		for (Map.Entry<String, String> entry : CATEGORY_REGEXES.entrySet()) {
			String categoryName = entry.getKey();
			String categoryRegex = entry.getValue();
			// Apply the regex to find the catgeory
			Pattern pattern = Pattern.compile(categoryRegex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(productName);
			if (matcher.find()) {
				return categoryName;
			}
		}
		return "others";
	}

	/**
	 * Get all the similar products with same category
	 * 
	 * @author Shahzada Khan
	 * @param product
	 * @throws IOException
	 * @return Map of products and price
	 */
	public static Map<String,Double> getSimilarProducts(String product) throws IOException {
		Map<String, Double> products = new HashMap<>();
		String category = categorizeProduct(product);
		// get the regex pattern for the product based on the category
		String categoryRegex = CATEGORY_REGEXES.get(category);
		Pattern pattern = Pattern.compile(categoryRegex, Pattern.CASE_INSENSITIVE);

		for (String fileName : filesList) {
			String website = webSiteName(fileName);
			File f = new File(fileName);
			BufferedReader brd = new BufferedReader(new FileReader(f));
			String line;
			// find all the product which matched the product in that category
			Pattern patternProduct = Pattern.compile(product, Pattern.CASE_INSENSITIVE);
			while ((line = brd.readLine()) != null) {
				String[] fields = line.toLowerCase().split("\\|");
				String cat = fields[0].trim();
				Matcher matcher = patternProduct.matcher(fields[1]);
				String categoryType = categorizeProduct(cat);
				if (categoryType == category && matcher.find()) {
					// Regex to extract only price
					 String pricePattern = "\\$\\s?(\\d+\\.\\d{2})";

				        Pattern r = Pattern.compile(pricePattern);
				        Matcher m = r.matcher(fields[2].trim());

				        while (m.find()) {
				            String priceString = m.group(1);
				            double price = Double.parseDouble(priceString);
				            // add products to hashmap
				            products.put(website + " - " + fields[1].trim() + " - " + fields[2].trim(), price) ;
				        }
					System.out.println(website + " - " + fields[1].trim() + " - " + fields[2].trim());
				}
			}
			brd.close();

		}
		return products;
	}

	/**
	 * Get list of products from the title of the page
	 * 
	 * @author Megha Bhargava
	 * @return HashMap of categories and their count
	 */
	public static HashMap<String, Integer> categoryList() {

		HashMap<String, Integer> categoryCount = new HashMap<String, Integer>();
		for (String fileName : filesList) {
			File f = new File(fileName);

			// Read file and count categories
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] fields = line.toLowerCase().split("\\|");
					String category = fields[0].trim();
					// check if the category is present in the Map
					// if present then increase the count
					if (categoryCount.containsKey(category)) {
						categoryCount.put(category, categoryCount.get(category) + 1);
					} else {
						// else add category and add count as 1
						categoryCount.put(category, 1);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return categoryCount;
	}

	/**
	 * Catgeories each product title list in to unique category of food
	 * 
	 * @author Megha Bhargava
	 * @return List of Categories
	 */
	public static String[] getCategories() {
		HashMap<String, Integer> cat = categoryList();
		HashSet<String> uniqueCat = new HashSet<String>();
		for (String c : cat.keySet()) {
			// Get the catgeory of the product and add to set
			String category = categorizeProduct(c);
//			System.out.println(c+"==> "+category);
			uniqueCat.add(category);
		}
		return uniqueCat.toArray(new String[uniqueCat.size()]);
	}

	/**
	 * Get the website name from the file name
	 * 
	 * @author Megha Bhargava
	 * @param fileName
	 * @return website name
	 */
	private static String webSiteName(String fileName) {
		switch (fileName) {
		case "saveonfoods.txt":
			return "Save On Foods - https://www.saveonfoods.com/";

		case "shopfoodex.txt":
			return "Shop Food Ex - https://www.shopfoodex.com/";

		case "yupik.txt":
			return "Yupik - https://yupik.com/en/";

		default:
			return "Unknown File";

		}
	}

	/**
	 * Get list of all products based on the category
	 * 
	 * @author Megha Bhargava
	 * @param category
	 * @throws IOException
	 * @return Map of products and price
	 */
	public static Map<String,Double>  getListOfProductByCategory(String category) throws IOException {
		Map<String, Double> products = new HashMap<>();
		// find the category to extract the regex
		String categoryRegex = CATEGORY_REGEXES.get(category);
		Pattern pattern = Pattern.compile(categoryRegex, Pattern.CASE_INSENSITIVE);

		for (String fileName : filesList) {
			String website = webSiteName(fileName);
			File f = new File(fileName);
			BufferedReader brd = new BufferedReader(new FileReader(f));
			String line;
			while ((line = brd.readLine()) != null) {
				String[] fields = line.toLowerCase().split("\\|");
				String cat = fields[0].trim();
				// get the product catgeoryType and match with category from the parameters
				String categoryType = categorizeProduct(cat);
				if (categoryType == category) {
					// pattern to extract price
					 String pricePattern = "\\$\\s?(\\d+\\.\\d{2})";

				        Pattern r = Pattern.compile(pricePattern);
				        Matcher m = r.matcher(fields[2].trim());

				        while (m.find()) {
				            String priceString = m.group(1);
				            double price = Double.parseDouble(priceString);
				            // add products to hashmap
				            products.put(website + " - " + fields[1].trim() + " - " + fields[2].trim(), price) ;
				        }
					System.out.println(website + " - " + fields[1].trim() + " - " + fields[2].trim());
				}
			}
			brd.close();

		}
		return products;
	}
}
