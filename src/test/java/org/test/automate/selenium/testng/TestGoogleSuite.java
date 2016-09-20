package org.test.automate.selenium.testng;

import java.io.Serializable;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.test.automate.selenium.testng.pageobject.*;
import org.test.automate.selenium.base.BaseDriverSetup;
import org.test.automate.selenium.base.BaseDriverSetup.JUnitSelenium;
import org.test.automate.selenium.template.CustomMethodDescriber;
import com.thoughtworks.selenium.Selenium;
import org.test.automate.selenium.testng.pageobject.manual.*;

public class TestGoogleSuite implements Serializable {

	/**
	 * Please don't change this template class
	 * 
	 **/

	private static final long serialVersionUID = 1L;

	private static Selenium selenium;
	private static WebDriver driver;
	private static boolean useRC = false;
	private static String baseUrl;
	private static CustomMethodDescriber describer;

	@BeforeClass
	public static void setUp() throws Exception {

		JUnitSelenium seleniumDriver = BaseDriverSetup.setUpDriver(useRC);
		baseUrl = seleniumDriver.getBaseUrl();
		describer = CustomMethodDescriber.getInstance();

		if (useRC) {
			selenium = seleniumDriver.getSelenium();
		} else {
			driver = seleniumDriver.getDriver();
		}

	}

	@After
	public void tearDown() throws Exception {

		System.out.println(" tear Down ");

		/*
		 * Thread.sleep(2000);
		 * 
		 * if(useRC){ selenium.click("//img[@alt='Logout']"); }else{
		 * driver.findElement(By.xpath("//img[@alt='Logout']")).click(); }
		 */

		Thread.currentThread().sleep(2000);
	}

	@AfterClass
	public static void closeBrowser() throws InterruptedException {

		if (useRC) {
			selenium.stop();
		} else {
			driver.quit();
		}

		Thread.currentThread().sleep(2000);
	}

	@Test
	public void test1() throws Exception {

		GooglePO1 googlePO1 = new GooglePO1(driver, describer);

		driver.get(baseUrl + "");
		String nameParam = "dhanuka";
		googlePO1.login1(nameParam);

		GooglePOManual googlePOManual = new GooglePOManual(driver, describer);

		String manualParam = "dhanuka";
		googlePOManual.loginM(manualParam);

	}
}
