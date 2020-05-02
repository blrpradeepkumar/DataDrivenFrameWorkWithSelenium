package com.pradeep.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.pradeep.base.TestBase;
import com.pradeep.utilities.TestUtil;

public class OpenAccountTest extends TestBase {

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dataProvider")
	public void openAccountTest(Hashtable<String, String> data) throws InterruptedException {
		click("openaccount_css");
		select("customer_css", data.get("customer"));
		select("currency_css", data.get("currency"));
		click("process_css");

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		// Assert.assertTrue(alert.getText().contains(alertText));
		alert.accept();

	}

}
