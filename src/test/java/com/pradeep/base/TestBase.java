package com.pradeep.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.pradeep.utilities.ExcelReader;
import com.pradeep.utilities.ExtentManager;
import com.pradeep.utilities.TestUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	/*
	 * WebDriver Properties Logs ExtentReports DB Excel Mail ReportNG ExtentReports
	 * Jenkins
	 */

	public static WebDriver driver;
	public static Properties propertyConfig = new Properties();
	public static Properties propertyOR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static ExcelReader excel = new ExcelReader(
			System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	public ExtentReports report = ExtentManager.getInstance();
	public static ExtentTest test;
	public static String browser;

	@BeforeSuite
	public void setUp() {

		if (driver == null) {
			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\config.properties");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				propertyConfig.load(fis);
				log.debug("Config file loaded");
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(System.getProperty("user.dir")
						+ "\\src\\test\\resources\\properties\\objectrepository.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				propertyOR.load(fis);
				log.debug("Object repository file loaded");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {
			browser = System.getenv("browser");
		} else {
			browser = propertyConfig.getProperty("browser");
		}
		
		propertyConfig.setProperty("browser", browser);

		if (propertyConfig.getProperty("browser").equals("firefox")) {
			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
			log.debug("Firefox launched");
		} else if (propertyConfig.getProperty("browser").equals("chrome")) {
			/*
			 * System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
			 * + "\\src\\test\\resources\\drivers\\chromedriver.exe");
			 */
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			log.debug("Chrome launched");
		} else if (propertyConfig.getProperty("browser").equals("ie")) {
			System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			log.debug("Internet Explorer launched");
		}

		driver.get(propertyConfig.getProperty("url"));
		log.debug("Launched url - " + propertyConfig.getProperty("url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(propertyConfig.getProperty("implicit.wait")),
				TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 5);

	}

	public void click(String locator) {
		if (locator.endsWith("_css")) {
			driver.findElement(By.cssSelector(propertyOR.getProperty(locator))).click();
		} else if (locator.endsWith("_xpath")) {
			driver.findElement(By.xpath(propertyOR.getProperty(locator))).click();
		} else if (locator.endsWith("_id")) {
			driver.findElement(By.id(propertyOR.getProperty(locator))).click();
		}
		test.log(LogStatus.INFO, "Clicking on : " + locator);
	}

	public void type(String locator, String value) {

		if (locator.endsWith("_css")) {
			driver.findElement(By.cssSelector(propertyOR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_xpath")) {
			driver.findElement(By.xpath(propertyOR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_id")) {
			driver.findElement(By.id(propertyOR.getProperty(locator))).sendKeys(value);
		}

		test.log(LogStatus.INFO, "Typing in : " + locator + " entered value as " + value);
	}

	public static void verifyEquals(String expected, String actual) throws IOException {
		try {
			Assert.assertEquals(actual, expected);
		} catch (Throwable t) {

			TestUtil.captureScreenshot();

			// ReportNG
			Reporter.log("<br>" + "Verification failure :" + t.getMessage() + "<br>");
			Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName
					+ " height= 200 width = 200</img></a>");
			Reporter.log("<br>");

			// Extent Reports
			test.log(LogStatus.FAIL, " Verification failed with exception " + t.getMessage());
			test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.screenshotName));
		}
	}

	static WebElement dropdown;

	public void select(String locator, String value) {
		if (locator.endsWith("_css")) {
			dropdown = driver.findElement(By.cssSelector(propertyOR.getProperty(locator)));
		} else if (locator.endsWith("_xpath")) {
			dropdown = driver.findElement(By.xpath(propertyOR.getProperty(locator)));
		} else if (locator.endsWith("_id")) {
			dropdown = driver.findElement(By.id(propertyOR.getProperty(locator)));
		}
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);

		test.log(LogStatus.INFO, "Selecting from dropdown : " + locator + " value as " + value);
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	@AfterSuite
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		log.debug("Test execution completed");

	}

}
