package com.pradeep.listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import com.pradeep.base.TestBase;
import com.pradeep.utilities.MonitoringMail;
import com.pradeep.utilities.TestConfig;
import com.pradeep.utilities.TestUtil;
import com.relevantcodes.extentreports.LogStatus;

public class CustomListeners extends TestBase implements ITestListener, ISuiteListener {

	public String messageBody;

	public void onFinish(ITestContext context) {
		MonitoringMail mail = new MonitoringMail();
		try {
			messageBody = "http://" + InetAddress.getLocalHost().getHostAddress()
					+ ":8080/job/DataDrivenFramework/ExtentReports/";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(messageBody);
		try {
			mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, messageBody);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	public void onTestFailure(ITestResult result) {
		// To display html view in html reports
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		try {
			TestUtil.captureScreenshot();
		} catch (IOException e) {
			e.printStackTrace();
		}

		test.log(LogStatus.FAIL, result.getName().toUpperCase() + " Failed with exception " + result.getThrowable());
		test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.screenshotName));

		Reporter.log("Click to see Screenshot");
		Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + ">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName
				+ " height= 200 width = 200</img></a>");
		report.endTest(test);
		report.flush();
	}

	public void onStart(ITestContext context) {
	}

	public void onTestFailedWithTimeout(ITestResult result) {
	}

	public void onTestSkipped(ITestResult result) {
		test.log(LogStatus.SKIP, result.getName().toUpperCase() + "Skipped the test as the run mode is N");
		report.endTest(test);
		report.flush();
	}

	public void onTestStart(ITestResult result) {
		test = report.startTest(result.getName().toUpperCase());

		// Run modes
		if (!TestUtil.isTestRunnable(result.getName(), excel)) {
			throw new SkipException(
					"Skipping the test" + result.getName().toUpperCase() + " as the Run mode is not true");
		}
	}

	public void onTestSuccess(ITestResult result) {
		test.log(LogStatus.PASS, result.getName().toUpperCase() + " PASS");
		report.endTest(test);
		report.flush();

	}

}