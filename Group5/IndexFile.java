package Group5;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.openqa.selenium.interactions.Actions;

import Actions.SpellChecking;
import Actions.WordCompletion;
import Actions.BestDeals;
import Actions.DataValidation;
import Actions.FrequencyCount;
import Actions.InvertedIndexing;
import Actions.PageRanking;
import Actions.ProductCategoryList;
import Actions.SearchFrequency;
import HtmlParsing.HtmlParsingSaveOnFoods;
import HtmlParsing.HtmlParsingShopFoodEx;
import HtmlParsing.HtmlParsingYupik;
import Webcrawling.WebCrawlerSaveOnFoods;
import Webcrawling.WebCrawlerShopFoodEx;
import Webcrawling.WebCrawlerYupik;

public class IndexFile {

	public static void main(String[] args) throws NumberFormatException, Exception {
		// TODO Auto-generated method stub
		// Websites ;links to Crawl
		System.out.println("Welcome to Uwin Food Cart\n");
		String url_Yupik = "https://yupik.com/en/";
		String url_ShopFoodEx = "https://www.shopfoodex.com/";
		String url_SaveOnFoods = "https://www.saveonfoods.com/";
		// Create a HashTable of all the links crawled
		Hashtable<String, String> url_Map = new Hashtable<String, String>();
		// Start web Crawling
		System.out.println("Do you wish to crawl. If Yes enter Y");
		Scanner inp = new Scanner(System.in);
		String ch = "N";
		ch=inp.nextLine();
		if (ch.equals("y") || ch.equals("Y")) {
			url_Map.putAll(WebCrawlerYupik.startWebCrawling(url_Yupik));
			url_Map.putAll(WebCrawlerShopFoodEx.startWebCrawling(url_ShopFoodEx));
			url_Map.putAll(WebCrawlerSaveOnFoods.startWebCrawling(url_SaveOnFoods));
		}

		System.out.println("Do you wish to perform data validation. If Yes then press Y");
		String dataValidate = inp.nextLine();
		if (dataValidate.equals("Y") || dataValidate.equals("y")) {
			// Data validation with regular expression
			DataValidation.dataValidation();
		}
		// Html Parsing on the data
		System.out.println("Do you wish to Parse. If Yes enter Y");
		ch=inp.nextLine();
		if (ch.equals("y") || ch.equals("Y")) {
			System.out.println("Parsing the website data. Please wait...");
			HtmlParsingYupik.parse();
			HtmlParsingShopFoodEx.parse();
			HtmlParsingSaveOnFoods.parse();
			System.out.println("\nDone with parsing");
		}
	
		// Craete a list of catgeory
		String[] listOfCategory = ProductCategoryList.getCategories();
		Map<String, Double> products = new HashMap<String, Double>();

		do {
			do {
				System.out.println("\nChoose the option\n1. Get of Products by Category\n2. Search product");
				switch (inp.nextLine()) {
				case "1":
					System.out.println("List of common categories");
					for (int i = 0; i < listOfCategory.length; i++) {
						System.out.println(i + 1 + ". " + listOfCategory[i]);
					}
					System.out.println("Select the category");
					ch = inp.nextLine();

					System.out.println("List of products:");
					// Get all the products of the selected category
					products = ProductCategoryList.getListOfProductByCategory(listOfCategory[Integer.parseInt(ch) - 1]);
					System.out.println("\nTop 5 products:");
					// Get 5 products from the list that are least expensive
					BestDeals.TopDeals(products);
					// Maintain search frequency
					SearchFrequency.SearchCount(listOfCategory[Integer.parseInt(ch) - 1]);
					break;
				case "2":
					boolean repeat = true;
					while (repeat) {
						System.out.println("Enter product name");
						String product = inp.nextLine();
						// Spell Checking of product
						boolean spellCheck = SpellChecking.SpellChecker(product);

						if (!spellCheck) {
							System.out.println("Please check the spelling");
							// Using word completion to give suggestions of words
							List<String> suggestedWords = WordCompletion.findSimilarWords(product);
							System.out.println("\nSome suggested words are:");
							for (String word : suggestedWords) {
								System.out.println(word);
							}
						} else {
							System.out.println("List of products: ");
							// Get the products that match the search key and its category
							products = ProductCategoryList.getSimilarProducts(product);
							System.out.println("\nTop 5 products:");
							// Get 5 products from the list that are least expensive
							BestDeals.TopDeals(products);
							System.out.println("\nFrequency Count:");
							// Inverted indexing and frequency count
							InvertedIndexing.Indexing(url_Map, product);
							// Maintain search frequency
							SearchFrequency.SearchCount(product);
							// Page ranking for the product
							System.out.println("Do you wish to page rank. If Yes enter Y");
							ch=inp.nextLine();
							if (ch.equals("y") || ch.equals("Y")) {
								PageRanking.pageRank(product);
							}
							repeat = false;
						}
					}
					break;
				default:
					System.out.println("Choose correct option\nPress y to continue");
					ch = inp.nextLine();
					break;
				}
			} while (ch.equals("Y") || ch.equals("y"));

			System.out.println("\nPress Y to continue");
			ch = inp.nextLine();
		} while (ch.equals("Y") || ch.equals("y"));
		System.out.println("Thank You");

	}

}
