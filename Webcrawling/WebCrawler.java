package Webcrawling;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.WebElement;

public class WebCrawler {
	/**
	 * take list of Web Elements and extract the href for all the a tag
	 * 
	 * @author Megha Bhargava
	 * @param list of WebElement
	 * @return list of urls
	 */
	public static List<String> findHyperLinks(List<WebElement> lnks) {
		List<String> url_List = new ArrayList<String>();
		for (WebElement elements : lnks) {

			// check for null
			if (elements.equals(null))
				continue;
			else {
				// If href present add to the list
				url_List.add(elements.getAttribute("href"));
			}
		}
		// remove if any null link
		url_List.remove(null);
		return (url_List);
	}

	/**
	 * Write the html file content from the web page crawled 
	 * 
	 * @param folderName - where the file to be created
	 * @param content - content to be written
	 * @param fileName - name for the file where the content saved
	 * @param extension - format to save .html or .txt
	 * @author Megha Bhargava
	 */
	public static void writeContent(String folderName, String content, String fileName, String extension) {
		try {
			File f = new File(folderName + fileName + extension);
			FileWriter writer = new FileWriter(f, false);
			writer.write(content);
//			System.out.println("Report Created is in Location : " + f.getAbsolutePath());
			writer.close();
		} catch (Exception e) {
			System.out.println("OOPS!!!Error in Writing file");
		}
	}

	/**
	 * Create the html file with the content from the web page crawled 
	 * 
	 * @author Megha Bhargava
	 * @param pageCount - to tract the number of pages
	 * @param url - url of page
	 * @param folderName - where the file to be created
	 * @param content - content to be written
	 * @param fileName - name for the file where the content saved
	 * @return Hashtable of all the urls
	 */
	public static Hashtable<String, String> createFile(int pageCount, String url, String content, String fileName,
			String folder) {
		Hashtable<String, String> url_Map = new Hashtable<String, String>();
		url_Map.put(fileName + pageCount + ".html", url);
		writeContent(folder, content, fileName + pageCount, ".html");
		return url_Map;
	}

}
