package com.pradeep.testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.pradeep.base.TestBase;

public class BankManagerLoginTest extends TestBase {

	@Test
	public void bankManagerLoginTest() throws InterruptedException, IOException {
		verifyEquals("abc", "xyz");
		log.debug("Inside LoginTest");
		click("bmlBtn_css");

		Assert.assertTrue(isElementPresent(By.cssSelector(propertyOR.getProperty("bmlBtn_css"))),
				"Login not successful");
		log.debug("LoginTest successfully executed");
		// Assert.fail();

	}

}