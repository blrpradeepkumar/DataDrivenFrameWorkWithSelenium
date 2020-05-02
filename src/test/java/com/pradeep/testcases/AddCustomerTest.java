package com.pradeep.testcases;

import java.util.Hashtable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.pradeep.base.TestBase;
import com.pradeep.utilities.TestUtil;

public class AddCustomerTest extends TestBase {

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dataProvider")
	public void addCustomerTest(Hashtable<String,String> data)
			throws InterruptedException {
		if (!data.get("Runmode").equals("Y")) {
			throw new SkipException("Skipping the test case as the run mode is N");
		}
		click("addCustomerButton_xpath");
		type("firstName_xpath", data.get("firstName"));
		type("lastName_xpath", data.get("lastName"));
		type("postCode_xpath", data.get("postCode"));
		click("addbtn_xpath");

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		Assert.assertTrue(alert.getText().contains(data.get("alertText")));
		alert.accept();

	}

}
