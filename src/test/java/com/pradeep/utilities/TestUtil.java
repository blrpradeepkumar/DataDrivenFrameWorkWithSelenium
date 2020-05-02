package com.pradeep.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.pradeep.base.TestBase;

public class TestUtil extends TestBase {

	public static String screenshotPath;
	public static String screenshotName;

	public static void captureScreenshot() throws IOException {
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Date date = new Date();
		screenshotName = date.toString().replace(":", "_").replace(" ", "_") + ".jpg";
		FileUtils.copyFile(screenshotFile,
				new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + screenshotName));

	}

	@DataProvider(name = "dataProvider")
	public Object[][] getData(Method methodName) {

		String sheetName = methodName.getName();
		int rows = excel.getRowCount(sheetName);
		int columns = excel.getColumnCount(sheetName);

		Object[][] data = new Object[rows - 1][1];
		Hashtable<String, String> table = null;

		for (int rowNum = 2; rowNum <= rows; rowNum++) {
			table = new Hashtable<String, String>();
			for (int colNum = 0; colNum < columns; colNum++) {
				// data[0][0]
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				data[rowNum - 2][0] = table;
				// data[rowNum - 2][colNum] = excel.getCellData(sheetName, colNum, rowNum);
			}
		}
		return data;

	}

	public static boolean isTestRunnable(String testName, ExcelReader excel) {
		String sheetName = "test_suite";
		int rows = excel.getRowCount(sheetName);

		for (int rowNum = 2; rowNum < rows; rowNum++) {
			String testCase = excel.getCellData(sheetName, "TCID", rowNum);
			if (testCase.equalsIgnoreCase(testName)) {
				String runMode = excel.getCellData(sheetName, "Runmode", rowNum);
				if (runMode.equalsIgnoreCase("Y"))
					return true;
				else
					return false;

			}
		}
		return false;
	}

}
