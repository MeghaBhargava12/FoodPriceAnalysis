package Webcrawling;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jsoup.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebCrawlerSaveOnFoods {
	private static List<WebElement> links = new ArrayList<WebElement>();
	private static List<String> url_List = new ArrayList<String>();
	private static Hashtable<String, String> url_Map = new Hashtable<String, String>();

	/**
	 * Perform webcrawling for the website "https://www.saveonfoods.com/" using edge
	 * driver in selenium
	 * 
	 * @author Megha Bhargava
	 * @param websiteUrl
	 * @return Hashtable of urls with respect to the file store
	 */
	public static Hashtable<String, String> startWebCrawling(String websiteUrl) {

		// Initializing Edge diver
		File file = new File("Driver\\msedgedriver.exe");
		System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
		EdgeOptions options = new EdgeOptions();
		options.setHeadless(true);

		// Create a new instance of the EdgeDriver with the headless option
		WebDriver driver = new EdgeDriver(options);
		driver.get(websiteUrl);

		// Get the target on the main page to extract the links
		links = driver.findElements(By.cssSelector("div[class*=\"StackedContentBlockImageContainer\"] a"));
		// Get all the anchor tags from the target
		url_List = WebCrawler.findHyperLinks(links);

		int counter = 0;
		for (String url : url_List) {
			try {
				// navigate to the links extracted from main page
				driver.navigate().to(url);
				String content = driver.getPageSource();
				List<WebElement> moreLinks = driver.findElements(By.cssSelector("div[class*=\"PillMenu\"] a"));
				List<String> url_List1 = WebCrawler.findHyperLinks(moreLinks);
				for (String u : url_List1) {

					driver.navigate().to(u);
					content = driver.getPageSource();
					// Navigate further to get the data of products
					List<WebElement> moreLinks1 = driver.findElements(By.cssSelector("ul[class*=\"PageLinks\"] a"));
					List<String> url_List2 = WebCrawler.findHyperLinks(moreLinks1);
					for (String u1 : url_List2) {

						driver.navigate().to(u1);
						content = driver.getPageSource();
						// Store the page
						url_Map.putAll(
								WebCrawler.createFile(counter++, url, content, "SaveOnFoods", "SaveOnFoodsHtml/"));
						moreLinks1 = driver.findElements(By.cssSelector("ul[class*=\"PageLinks\"] a"));
						List<String> url_List3 = WebCrawler.findHyperLinks(moreLinks1);
						for (String u2 : url_List3) {
							if (!url_List2.contains(u2)) {

								driver.navigate().to(u1);
								content = driver.getPageSource();
								url_Map.putAll(WebCrawler.createFile(counter++, url, content, "SaveOnFoods",
										"SaveOnFoodsHtml/"));
							}
						}
					}

				}
				// If you wish to extract only a limited set of pages from the website
				if (counter >= 5) {
					break;
				}
			} catch (Exception e) {
				continue;
			}
		}
		driver.quit();
		return url_Map;
	}
}
