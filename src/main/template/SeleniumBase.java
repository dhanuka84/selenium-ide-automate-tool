package org.test.automate.selenium.template;

import java.io.Serializable;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import org.test.automate.selenium.base.BaseDriverSetup;
import org.test.automate.selenium.base.BaseDriverSetup.JUnitSelenium;
import org.test.automate.selenium.template.CustomMethodDescriber;
import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.*;
import com.thoughtworks.selenium.*;
import org.testng.annotations.*;
import static org.testng.Assert.*;


public class SeleniumBase extends BaseDriverSetup implements Serializable{
	
	/**
	 * Please don't change this template class
	 * 
	 * **/

	private static final long serialVersionUID = 1L;
	
	private static Selenium selenium;
	private static WebDriver driver;
	private static boolean useRC = false;
	private static String baseUrl;
	private static CustomMethodDescriber describer;
	
	
	

	@BeforeClass
	public void setUp() throws Exception {
		JUnitSelenium seleniumDriver = BaseDriverSetup.setUpDriver(useRC);	
		baseUrl = seleniumDriver.getBaseUrl();
		describer = CustomMethodDescriber.getInstance();
		
		if(useRC){
			selenium = seleniumDriver.getSelenium();
		}else{
			driver = seleniumDriver.getDriver();
		}
	}
	
	@AfterMethod
	public void tearDown() throws Exception {
		
		System.out.println(" tear Down ");

		Thread.sleep(2000);
		
		if(useRC){
			selenium.click("//img[@alt='Logout']");
		}else{
			driver.findElement(By.xpath("//img[@alt='Logout']")).click();
		}
		
		
		Thread.currentThread().sleep(2000);
	}
	
	@AfterClass
	public void closeBrowser() throws InterruptedException{
		if(useRC){
			selenium.stop();
		}else{
			driver.quit();
		}
		
		Thread.currentThread().sleep(2000);
	}
	
	
	
	

}
