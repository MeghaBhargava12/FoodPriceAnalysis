package Webcrawling;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class WebCrawlerYupik {
	private static List<WebElement> links = new ArrayList<WebElement>();
	private static List<String> url_List = new ArrayList<String>();
	private static Hashtable<String, String> url_Map = new Hashtable<String, String>();
	/**
	 * Perform webcrawling for the website "https://yupik.com/en/" using edge driver in selenium
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
		WebElement results = driver.findElement(By.cssSelector("nav.navigation"));
		// Get all the anchor tags from the target 
		links = results.findElements(
				By.cssSelector("ul#topmenu > li.level-top > ul.submenu > li.first > ul.submenu > li > a"));

		// Get all the links from anchor tag
		url_List = WebCrawler.findHyperLinks(links);
		Set<String> mySet = new HashSet<String>(url_List);
		url_List = new ArrayList<String>(mySet);

		int counter = 0;
		for (String url : url_List) {
			try {
				// navigate to the links extracted from main page
				driver.navigate().to(url);
				String content = driver.getPageSource();
				// Store the page
				url_Map.putAll(WebCrawler.createFile(counter++, url, content, "Yupik", "YupikHtml/"));
				// Get more links in the page
				List<WebElement> more = driver.findElements(By.cssSelector("ul.pages-items"));
				if (!more.isEmpty()) {
					for (WebElement e : more) {
						List<WebElement> moreLinks = e.findElements(By.tagName("a"));
						List<String> url_List1 = WebCrawler.findHyperLinks(moreLinks);
						mySet = new HashSet<String>(url_List1);
						url_List1 = new ArrayList<String>(mySet);
						// Get more pages
						for (String u : mySet) {
							driver.navigate().to(u);
							content = driver.getPageSource();
							// Store the page
							url_Map.putAll(WebCrawler.createFile(counter++, u, content, "Yupik", "YupikHtml/"));
						}
					}
				}
				// If you wish to extract only a limited set of pages from the website
				if(counter>=5) {
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
