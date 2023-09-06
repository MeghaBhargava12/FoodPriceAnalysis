package Actions;

/*
 * Validate the language and title of web pages
 * @author Peiyi Ding
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
	public static String innerContent(String fileName, String folderName) throws IOException {

		File file = new File("../FoodPriceAnalysis/" + folderName + "/" + fileName);

		FileInputStream fIStream;
		StringBuilder stringBuilder = new StringBuilder();
		fIStream = new FileInputStream(file);
		int temp = 0;
		while ((temp = fIStream.read()) != -1) {
			stringBuilder.append((char) temp);
		}
		return stringBuilder.toString();
	}

	public ArrayList<String> fileNameList(String folderName) {
		ArrayList<String> fNameList = new ArrayList<String>();
		String path = "../FoodPriceAnalysis/" + folderName;
		File file = new File(path);

		File[] filelist = file.listFiles();

		for (File f : filelist) {
			fNameList.add(f.getName());

		}
		return fNameList;
	}

	public static void dataValidation() throws IOException {
		DataValidation dataValidation = new DataValidation();

		// Regular Expression
		String titlePattern = "<title>([\\s\\S]*?)</title>";
		String langPattern = "lang=\"([\\s\\S]*?)\"";

		String[] folder = { "SaveOnFoodsHtml", "ShopFoodExHtml", "YupikHtml" };

		for (String folderName : folder) {
			System.out.println("Now start to validate the title and lang of files in \"" + folderName + "\" folder.");

			ArrayList<String> fNameList = new ArrayList<>();
			fNameList.addAll(dataValidation.fileNameList(folderName));

			for (String fNL : fNameList) {
				String fileText = innerContent(fNL, folderName);

				// Create Pattern object
				Pattern r1 = Pattern.compile(titlePattern);
				Pattern r2 = Pattern.compile(langPattern);

				// Create matcher object.
				Matcher m1 = r1.matcher(fileText);
				Matcher m2 = r2.matcher(fileText);

				while (m1.find() & m2.find()) {
					System.out.println("File name is " + fNL + " Found value: title= \"" + m1.group(1) + "\"; lang= \""
							+ m2.group(1) + "\"");
					break;
				}

			} // for (String fNL : fNameList)
			System.out.println("Print of folder: \"" + folderName + "\" has finished.");
		} // for (String folderName : folder)
	}

	public static void main(String[] args) throws IOException {
		DataValidation.dataValidation();
	}

}// all
